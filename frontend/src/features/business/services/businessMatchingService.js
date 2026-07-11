import apiClient from '@/api/axios';

export const businessMatchingService = {
  getMatchesForShipment: async (shipmentId) => {
    const response = await apiClient.get('/v1/matches/shipment/' + shipmentId);
    return response.data;
  },
  acceptBid: async (matchId) => {
    const response = await apiClient.post('/v1/matches/' + matchId + '/accept');
    return response.data;
  },
  rejectBid: async (matchId) => {
    const response = await apiClient.post('/v1/matches/' + matchId + '/reject');
    return response.data;
  }
};
