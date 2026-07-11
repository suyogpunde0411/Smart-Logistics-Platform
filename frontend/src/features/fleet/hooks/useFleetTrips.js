import { useQuery } from '@tanstack/react-query';
import { fleetTripService } from '../services/fleetTripService';

export const useFleetTrips = (params) => {
  return useQuery({
    queryKey: ['fleetTrips', params],
    queryFn: () => fleetTripService.getTrips(params),
  });
};
