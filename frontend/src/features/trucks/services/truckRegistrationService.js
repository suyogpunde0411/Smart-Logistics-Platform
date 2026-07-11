import apiClient from '@/api/axios';

export const truckRegistrationService = {
  createTruck: async (truckData) => {
    // Expected to handle the massive multi-step payload
    const response = await apiClient.post('/v1/trucks', truckData);
    return response.data;
  }
};
