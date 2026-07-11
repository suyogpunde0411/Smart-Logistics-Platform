import { useQuery } from '@tanstack/react-query';
import { fleetTruckService } from '../services/fleetTruckService';

export const useFleetSummary = () => {
  return useQuery({
    queryKey: ['fleetSummary'],
    queryFn: fleetTruckService.getFleetSummary,
  });
};

export const useFleetTrucks = (params) => {
  return useQuery({
    queryKey: ['fleetTrucks', params],
    queryFn: () => fleetTruckService.getTrucks(params),
  });
};
