import apiClient from '@/api/axios';

export const businessAnalyticsService = {
  getAnalytics: async () => {
    const response = await apiClient.get('/v1/analytics/business/me');
    return response.data;
  }
};
