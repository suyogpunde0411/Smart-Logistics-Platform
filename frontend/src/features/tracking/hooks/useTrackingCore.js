import { useQuery } from '@tanstack/react-query';
import { trackingCoreService } from '../services/trackingCoreService';

export const useActiveTrips = (params) => {
  return useQuery({
    queryKey: ['activeTrips', params],
    queryFn: () => trackingCoreService.getActiveTrips(params),
    refetchInterval: 60000 // Refresh list every 60s
  });
};

export const useTripDetails = (tripId) => {
  return useQuery({
    queryKey: ['tripDetails', tripId],
    queryFn: () => trackingCoreService.getTripDetails(tripId),
    enabled: !!tripId,
  });
};

export const useTripTimeline = (tripId) => {
  return useQuery({
    queryKey: ['tripTimeline', tripId],
    queryFn: () => trackingCoreService.getTripTimeline(tripId),
    enabled: !!tripId,
    refetchInterval: 60000 // Fetch new events every 60s
  });
};
