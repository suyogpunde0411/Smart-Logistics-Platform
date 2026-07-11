import { useQuery } from '@tanstack/react-query';
import { matchAnalyticsService } from '../services/matchAnalyticsService';

export const useMatchAnalytics = () => {
  return useQuery({
    queryKey: ['matchAnalytics'],
    queryFn: () => matchAnalyticsService.getMatchAnalytics(),
  });
};

export const useMatchHistory = (params) => {
  return useQuery({
    queryKey: ['matchHistory', params],
    queryFn: () => matchAnalyticsService.getMatchHistory(params),
  });
};
