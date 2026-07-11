import apiClient from '@/api/axios';

export const trackingCoreService = {
  getActiveTrips: async (params) => {
    const response = await apiClient.get('/v1/tracking/trips', { params });
    return response.data;
  },
  getTripDetails: async (tripId) => {
    const response = await apiClient.get(`/v1/tracking/trips/${tripId}`);
    return response.data;
  },
  getTripTimeline: async (tripId) => {
    const response = await apiClient.get(`/v1/tracking/trips/${tripId}/timeline`);
    return response.data;
  }
};
