import apiClient from '@/api/axios';

const AUTH_URL = '/v1/auth';

export const authService = {
  register: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/register`, data);
    return response.data;
  },

  login: async (credentials) => {
    const response = await apiClient.post(`${AUTH_URL}/login`, credentials);
    return response.data;
  },

  refreshToken: async (tokenData) => {
    // Assuming backend takes { refreshToken: '...' }
    const response = await apiClient.post(`${AUTH_URL}/refresh`, tokenData);
    return response.data;
  },

  logout: async () => {
    const response = await apiClient.post(`${AUTH_URL}/logout`);
    return response.data;
  },

  forgotPassword: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/forgot-password`, data);
    return response.data;
  },

  resetPassword: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/reset-password`, data);
    return response.data;
  },

  changePassword: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/change-password`, data);
    return response.data;
  },

  sendEmailVerification: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/send-email-verification`, data);
    return response.data;
  },

  verifyEmail: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/verify-email`, data);
    return response.data;
  },

  sendOtp: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/send-otp`, data);
    return response.data;
  },

  verifyOtp: async (data) => {
    const response = await apiClient.post(`${AUTH_URL}/verify-otp`, data);
    return response.data;
  },

  getCurrentUser: async () => {
    const response = await apiClient.get(`${AUTH_URL}/me`);
    return response.data;
  },
};
