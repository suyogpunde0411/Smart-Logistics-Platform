import { useQuery } from '@tanstack/react-query';
import { fleetAnalyticsService } from '../services/fleetAnalyticsService';

export const useFleetAnalytics = () => {
  return useQuery({
    queryKey: ['fleetAnalytics'],
    queryFn: fleetAnalyticsService.getAnalytics,
  });
};
