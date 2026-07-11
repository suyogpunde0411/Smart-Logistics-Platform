import apiClient from '@/api/axios';

export const shipmentCoreService = {
  getShipments: async (params) => {
    const response = await apiClient.get('/v1/shipments', { params });
    return response.data;
  },
  getShipmentById: async (id) => {
    const response = await apiClient.get(`/v1/shipments/${id}`);
    return response.data;
  },
  updateShipmentStatus: async (id, status) => {
    const response = await apiClient.patch(`/v1/shipments/${id}/status`, { status });
    return response.data;
  },
  deleteShipment: async (id) => {
    const response = await apiClient.delete(`/v1/shipments/${id}`);
    return response.data;
  }
};
