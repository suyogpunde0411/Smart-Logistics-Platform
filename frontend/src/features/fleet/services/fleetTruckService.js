import apiClient from '@/api/axios';

export const fleetTruckService = {
  getFleetSummary: async () => {
    const response = await apiClient.get('/v1/fleet/summary');
    return response.data;
  },
  getTrucks: async (params) => {
    const response = await apiClient.get('/v1/trucks/fleet/me', { params });
    return response.data;
  },
  addTruck: async (truckData) => {
    const response = await apiClient.post('/v1/trucks', truckData);
    return response.data;
  }
};
