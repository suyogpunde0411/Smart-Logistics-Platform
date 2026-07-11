import apiClient from '@/api/axios';

export const fleetAnalyticsService = {
  getAnalytics: async () => {
    const response = await apiClient.get('/v1/analytics/fleet/me');
    return response.data;
  }
};
