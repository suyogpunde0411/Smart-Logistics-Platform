import apiClient from '@/api/axios';

export const fleetDriverService = {
  getDrivers: async (params) => {
    const response = await apiClient.get('/v1/fleet/drivers', { params });
    return response.data;
  },
  assignDriver: async (truckId, driverId) => {
    const response = await apiClient.post('/v1/fleet/assign', { truckId, driverId });
    return response.data;
  }
};
