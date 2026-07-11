import apiClient from '@/api/axios';

export const driverTruckService = {
  getMyTruck: async () => {
    const response = await apiClient.get('/v1/trucks/me');
    return response.data;
  }
};
