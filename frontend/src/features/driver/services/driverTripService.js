import apiClient from '@/api/axios';

export const driverTripService = {
  getMyTrips: async () => {
    const response = await apiClient.get('/v1/trips/me');
    return response.data;
  },
  updateTripStatus: async (tripId, status) => {
    const response = await apiClient.patch('/v1/trips/' + tripId + '/status', { status });
    return response.data;
  }
};
