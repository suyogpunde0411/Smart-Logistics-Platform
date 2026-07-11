import apiClient from '@/api/axios';

export const preferencesService = {
  getGlobalPreferences: async () => {
    const response = await apiClient.get('/v1/profile/preferences');
    return response.data;
  },
  updateGlobalPreferences: async (prefsData) => {
    const response = await apiClient.put('/v1/profile/preferences', prefsData);
    return response.data;
  }
};
