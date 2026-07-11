import apiClient from '@/api/axios';

export const driverProfileService = {
  getProfile: async () => {
    const response = await apiClient.get('/v1/users/me');
    return response.data;
  },
  updateProfile: async (data) => {
    const response = await apiClient.put('/v1/users/me', data);
    return response.data;
  },
  updateAvailability: async (isAvailable) => {
    const response = await apiClient.put('/v1/users/me/availability', { isAvailable });
    return response.data;
  }
};
