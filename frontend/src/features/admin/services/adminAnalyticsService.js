import apiClient from '@/api/axios';

export const adminAnalyticsService = {
  getPlatformKpis: async () => {
    const response = await apiClient.get('/v1/admin/analytics/kpis');
    return response.data;
  }
};
