import { useQuery } from '@tanstack/react-query';
import { trackingLocationService } from '../services/trackingLocationService';

export const useLiveLocation = (tripId) => {
  return useQuery({
    queryKey: ['liveLocation', tripId],
    queryFn: () => trackingLocationService.getLiveLocation(tripId),
    enabled: !!tripId,
    refetchInterval: 10000, // Fetch new GPS pings every 10 seconds (Simulated Real-Time)
    refetchIntervalInBackground: true
  });
};

export const useETA = (tripId) => {
  return useQuery({
    queryKey: ['liveETA', tripId],
    queryFn: () => trackingLocationService.getETA(tripId),
    enabled: !!tripId,
    refetchInterval: 30000 // Recalculate ETA every 30 seconds
  });
};
