import axios from 'axios';
import { store } from '@/redux/store';
import { logout, setCredentials, setSessionExpired } from '@/redux/slices/authSlice';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

apiClient.interceptors.request.use(
  (config) => {
    const state = store.getState();
    const token = state.auth.accessToken || state.auth.token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    if (!originalRequest) return Promise.reject(error);

    // Ignore 401s on the refresh endpoint or login endpoint
    if (originalRequest.url?.includes('/auth/login') || originalRequest.url?.includes('/auth/refresh')) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise(function (resolve, reject) {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers['Authorization'] = 'Bearer ' + token;
            return apiClient(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      const state = store.getState();
      const refreshToken = state.auth.refreshToken;

      if (!refreshToken) {
        store.dispatch(logout());
        store.dispatch(setSessionExpired(true));
        window.location.href = '/session-expired';
        return Promise.reject(error);
      }

      try {
        const { data } = await axios.post(`${apiClient.defaults.baseURL}/v1/auth/refresh`, {
          refreshToken,
        });
        
        // Update the store
        store.dispatch(setCredentials({
            user: state.auth.user,
            accessToken: data.accessToken,
            refreshToken: data.refreshToken || refreshToken
        }));
        
        apiClient.defaults.headers.common['Authorization'] = 'Bearer ' + data.accessToken;
        originalRequest.headers['Authorization'] = 'Bearer ' + data.accessToken;
        
        processQueue(null, data.accessToken);
        return apiClient(originalRequest);
      } catch (err) {
        processQueue(err, null);
        store.dispatch(logout());
        store.dispatch(setSessionExpired(true));
        window.location.href = '/session-expired';
        return Promise.reject(err);
      } finally {
        isRefreshing = false;
      }
    }
    
    if (error.response?.status === 403) {
      window.location.href = '/unauthorized';
    }

    return Promise.reject(error);
  }
);

export default apiClient;
