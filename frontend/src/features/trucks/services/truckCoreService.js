import apiClient from '@/api/axios';

export const truckCoreService = {
  getTrucks: async (params) => {
    const response = await apiClient.get('/v1/trucks', { params });
    return response.data;
  },
  getTruckById: async (truckId) => {
    const response = await apiClient.get(`/v1/trucks/${truckId}`);
    return response.data;
  },
  updateTruck: async (truckId, data) => {
    const response = await apiClient.patch(`/v1/trucks/${truckId}`, data);
    return response.data;
  },
  deleteTruck: async (truckId) => {
    const response = await apiClient.delete(`/v1/trucks/${truckId}`);
    return response.data;
  }
};
