import apiClient from '@/api/axios';

export const businessProfileService = {
  getProfile: async () => {
    const response = await apiClient.get('/v1/business/profile');
    return response.data;
  },
  updateProfile: async (data) => {
    const response = await apiClient.put('/v1/business/profile', data);
    return response.data;
  }
};
