import apiClient from '@/api/axios';

export const trackingLocationService = {
  getLiveLocation: async (tripId) => {
    // Fetches the latest GPS ping, current speed, and heading
    const response = await apiClient.get(`/v1/tracking/trips/${tripId}/location/live`);
    return response.data;
  },
  getETA: async (tripId) => {
    // Calculates or fetches the remaining time and distance
    const response = await apiClient.get(`/v1/tracking/trips/${tripId}/eta`);
    return response.data;
  }
};
