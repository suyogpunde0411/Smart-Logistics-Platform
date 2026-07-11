import { useQuery } from '@tanstack/react-query';
import { fleetDriverService } from '../services/fleetDriverService';

export const useFleetDrivers = (params) => {
  return useQuery({
    queryKey: ['fleetDrivers', params],
    queryFn: () => fleetDriverService.getDrivers(params),
  });
};
