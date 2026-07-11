import apiClient from '@/api/axios';

export const matchRecommendationService = {
  getRecommendedShipments: async (truckId) => {
    // For drivers/fleets to find shipments for a specific truck
    const response = await apiClient.get(`/v1/matching/recommendations/shipments`, { params: { truckId } });
    return response.data;
  },
  getRecommendedTrucks: async (shipmentId) => {
    // For business owners to find trucks for a specific shipment
    const response = await apiClient.get(`/v1/matching/recommendations/trucks`, { params: { shipmentId } });
    return response.data;
  },
  getMatchDetails: async (matchId) => {
    const response = await apiClient.get(`/v1/matching/${matchId}`);
    return response.data;
  }
};
