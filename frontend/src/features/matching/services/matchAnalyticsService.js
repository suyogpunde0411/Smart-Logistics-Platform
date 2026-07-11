import apiClient from '@/api/axios';

export const matchAnalyticsService = {
  getMatchAnalytics: async () => {
    // For Admins/Super Admins to view global matching performance
    const response = await apiClient.get(`/v1/matching/analytics`);
    return response.data;
  },
  getMatchHistory: async (params) => {
    // For viewing historical matches (completed, expired, rejected)
    const response = await apiClient.get(`/v1/matching/history`, { params });
    return response.data;
  }
};
