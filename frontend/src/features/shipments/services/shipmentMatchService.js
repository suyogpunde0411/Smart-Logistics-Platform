import apiClient from '@/api/axios';

export const shipmentMatchService = {
  getRecommendedMatches: async (shipmentId) => {
    const response = await apiClient.get(`/v1/shipments/${shipmentId}/matches/recommended`);
    return response.data;
  },
  getBids: async (shipmentId) => {
    const response = await apiClient.get(`/v1/shipments/${shipmentId}/bids`);
    return response.data;
  },
  acceptBid: async (shipmentId, bidId) => {
    const response = await apiClient.post(`/v1/shipments/${shipmentId}/bids/${bidId}/accept`);
    return response.data;
  },
  rejectBid: async (shipmentId, bidId) => {
    const response = await apiClient.post(`/v1/shipments/${shipmentId}/bids/${bidId}/reject`);
    return response.data;
  }
};
