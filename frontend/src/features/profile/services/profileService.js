import apiClient from '@/api/axios';

export const profileService = {
  getProfile: async () => {
    const response = await apiClient.get('/v1/profile/me');
    return response.data;
  },
  updateProfile: async (profileData) => {
    const response = await apiClient.put('/v1/profile/me', profileData);
    return response.data;
  },
  uploadAvatar: async (file) => {
    const formData = new FormData();
    formData.append('avatar', file);
    const response = await apiClient.post('/v1/profile/me/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  },
  deleteAvatar: async () => {
    const response = await apiClient.delete('/v1/profile/me/avatar');
    return response.data;
  }
};
