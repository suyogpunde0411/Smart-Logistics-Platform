import { useQuery } from '@tanstack/react-query';
import { driverAnalyticsService } from '../services/driverAnalyticsService';

export const useDriverAnalytics = () => {
  return useQuery({
    queryKey: ['driverAnalytics'],
    queryFn: driverAnalyticsService.getAnalytics,
  });
};
