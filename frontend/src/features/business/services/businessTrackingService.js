import apiClient from '@/api/axios';

export const businessTrackingService = {
  getShipmentTracking: async (shipmentId) => {
    const response = await apiClient.get('/v1/tracking/shipment/' + shipmentId);
    return response.data;
  }
};
