import apiClient from '@/api/axios';

export const fleetTripService = {
  getTrips: async (params) => {
    const response = await apiClient.get('/v1/trips/fleet/me', { params });
    return response.data;
  }
};
