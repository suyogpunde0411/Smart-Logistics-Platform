import apiClient from '@/api/axios';

export const adminSystemService = {
  getSettings: async () => {
    const response = await apiClient.get('/v1/admin/settings');
    return response.data;
  },
  updateSetting: async (key, value) => {
    const response = await apiClient.patch('/v1/admin/settings/' + key, { value });
    return response.data;
  }
};
