import apiClient from '@/api/axios';

export const notificationPreferencesService = {
  getPreferences: async () => {
    const response = await apiClient.get('/v1/notifications/preferences');
    return response.data;
  },
  updatePreferences: async (preferencesData) => {
    const response = await apiClient.put('/v1/notifications/preferences', preferencesData);
    return response.data;
  }
};
