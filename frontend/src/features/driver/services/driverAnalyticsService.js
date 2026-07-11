import apiClient from '@/api/axios';

export const driverAnalyticsService = {
  getAnalytics: async () => {
    const response = await apiClient.get('/v1/analytics/driver/me');
    return response.data;
  }
};
