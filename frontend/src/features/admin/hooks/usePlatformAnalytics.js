import { useQuery } from '@tanstack/react-query';
import { adminAnalyticsService } from '../services/adminAnalyticsService';

export const usePlatformKpis = () => {
  return useQuery({
    queryKey: ['platformKpis'],
    queryFn: adminAnalyticsService.getPlatformKpis,
  });
};
