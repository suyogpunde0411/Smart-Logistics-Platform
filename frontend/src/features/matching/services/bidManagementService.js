import apiClient from '@/api/axios';

export const bidManagementService = {
  submitBid: async (shipmentId, bidData) => {
    // For drivers/fleets to bid on a shipment
    const response = await apiClient.post(`/v1/shipments/${shipmentId}/bids`, bidData);
    return response.data;
  },
  updateBid: async (shipmentId, bidId, bidData) => {
    const response = await apiClient.put(`/v1/shipments/${shipmentId}/bids/${bidId}`, bidData);
    return response.data;
  },
  withdrawBid: async (shipmentId, bidId) => {
    const response = await apiClient.delete(`/v1/shipments/${shipmentId}/bids/${bidId}`);
    return response.data;
  },
  getBidsForShipment: async (shipmentId) => {
    // For business owners to view received bids
    const response = await apiClient.get(`/v1/shipments/${shipmentId}/bids`);
    return response.data;
  },
  getMyBids: async () => {
    // For drivers to view bids they've submitted across all shipments
    const response = await apiClient.get(`/v1/bids/me`);
    return response.data;
  }
};
