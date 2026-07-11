import { useQuery } from '@tanstack/react-query';
import { fleetMaintenanceService } from '../services/fleetMaintenanceService';

export const useFleetMaintenance = (params) => {
  return useQuery({
    queryKey: ['fleetMaintenance', params],
    queryFn: () => fleetMaintenanceService.getMaintenanceLogs(params),
  });
};
