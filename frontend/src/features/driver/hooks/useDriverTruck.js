import { useQuery } from '@tanstack/react-query';
import { driverTruckService } from '../services/driverTruckService';

export const useDriverTruck = () => {
  return useQuery({
    queryKey: ['driverTruck'],
    queryFn: driverTruckService.getMyTruck,
  });
};
