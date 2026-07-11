import apiClient from '@/api/axios';

export const driverShipmentService = {
  getAvailableShipments: async (params) => {
    const response = await apiClient.get('/v1/shipments/available', { params });
    return response.data;
  },
  getShipmentDetails: async (id) => {
    const response = await apiClient.get('/v1/shipments/' + id);
    return response.data;
  },
  requestMatch: async (shipmentId, bidAmount) => {
    const response = await apiClient.post('/v1/matches/request', { shipmentId, bidAmount });
    return response.data;
  },
  getMyMatches: async () => {
    const response = await apiClient.get('/v1/matches/me');
    return response.data;
  }
};
