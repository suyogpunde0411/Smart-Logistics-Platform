import { useQuery } from '@tanstack/react-query';
import { businessAnalyticsService } from '../services/businessAnalyticsService';

export const useBusinessAnalytics = () => {
  return useQuery({
    queryKey: ['businessAnalytics'],
    queryFn: businessAnalyticsService.getAnalytics,
  });
};
