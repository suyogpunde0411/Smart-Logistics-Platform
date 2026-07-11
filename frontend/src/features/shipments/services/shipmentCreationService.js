import apiClient from '@/api/axios';

export const shipmentCreationService = {
  createShipment: async (shipmentData) => {
    const response = await apiClient.post('/v1/shipments', shipmentData);
    return response.data;
  }
};
