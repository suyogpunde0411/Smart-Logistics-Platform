import { useQuery } from '@tanstack/react-query';
import { analyticsService } from '../services/analyticsService';

export const analyticsKeys = {
  dashboard: ['analytics', 'dashboard'],
  kpis: ['analytics', 'kpis'],
  routes: ['analytics', 'top-routes'],
  trips: (filters) => ['analytics', 'trips', filters],
};

export const useDashboard = () => useQuery({ queryKey: analyticsKeys.dashboard, queryFn: analyticsService.getDashboard });
export const useKPIs = (enabled) => useQuery({ queryKey: analyticsKeys.kpis, queryFn: analyticsService.getKpis, enabled });
export const useTopRoutes = (enabled) => useQuery({ queryKey: analyticsKeys.routes, queryFn: analyticsService.getTopRoutes, enabled });
export const useTrips = (filters, enabled = true) => useQuery({
  queryKey: analyticsKeys.trips(filters), queryFn: () => analyticsService.getTrips(filters), enabled,
});

// Feature entry point retained for consumers that need the dashboard query result.
export const useAnalytics = useDashboard;
