import apiClient from '@/api/axios';

export const businessShipmentService = {
  getShipments: async (params) => {
    const response = await apiClient.get('/v1/shipments/business/me', { params });
    return response.data;
  },
  getShipmentDetails: async (id) => {
    const response = await apiClient.get('/v1/shipments/' + id);
    return response.data;
  },
  createShipment: async (shipmentData) => {
    // If we have files, we might use FormData, but let's assume JSON first unless multipart is explicitly required.
    // We'll prepare it for FormData just in case, but usually multipart requires headers.
    const isFormData = shipmentData instanceof FormData;
    const response = await apiClient.post('/v1/shipments', shipmentData, {
      headers: isFormData ? { 'Content-Type': 'multipart/form-data' } : {}
    });
    return response.data;
  },
  updateShipmentStatus: async (id, status) => {
    const response = await apiClient.patch('/v1/shipments/' + id + '/status', { status });
    return response.data;
  }
};
