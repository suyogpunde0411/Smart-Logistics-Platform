import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { truckMaintenanceService } from '../services/truckMaintenanceService';
import toast from 'react-hot-toast';

export const useTruckMaintenance = (truckId) => {
  return useQuery({
    queryKey: ['truckMaintenance', truckId],
    queryFn: () => truckMaintenanceService.getMaintenanceLogs(truckId),
    enabled: !!truckId,
  });
};

export const useLogMaintenance = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ truckId, logData }) => truckMaintenanceService.logMaintenance(truckId, logData),
    onSuccess: (_, { truckId }) => {
      toast.success('Maintenance record added');
      queryClient.invalidateQueries({ queryKey: ['truckMaintenance', truckId] });
    },
    onError: () => toast.error('Failed to log maintenance')
  });
};
