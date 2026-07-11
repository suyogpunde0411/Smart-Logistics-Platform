import apiClient from '@/api/axios';

export const shipmentTrackingService = {
  getTimeline: async (shipmentId) => {
    const response = await apiClient.get(`/v1/shipments/${shipmentId}/timeline`);
    return response.data;
  },
  getLiveTracking: async (shipmentId) => {
    const response = await apiClient.get(`/v1/shipments/${shipmentId}/tracking`);
    return response.data;
  }
};
