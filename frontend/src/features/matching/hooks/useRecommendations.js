import { useQuery } from '@tanstack/react-query';
import { matchRecommendationService } from '../services/matchRecommendationService';

export const useRecommendedShipments = (truckId) => {
  return useQuery({
    queryKey: ['recommendedShipments', truckId],
    queryFn: () => matchRecommendationService.getRecommendedShipments(truckId),
    enabled: !!truckId,
    refetchInterval: 60000 // Auto-refresh recommendations every minute
  });
};

export const useRecommendedTrucks = (shipmentId) => {
  return useQuery({
    queryKey: ['recommendedTrucks', shipmentId],
    queryFn: () => matchRecommendationService.getRecommendedTrucks(shipmentId),
    enabled: !!shipmentId,
    refetchInterval: 60000 
  });
};

export const useMatchDetails = (matchId) => {
  return useQuery({
    queryKey: ['matchDetails', matchId],
    queryFn: () => matchRecommendationService.getMatchDetails(matchId),
    enabled: !!matchId,
  });
};
