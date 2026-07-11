import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { truckCoreService } from '../services/truckCoreService';
import toast from 'react-hot-toast';

export const useTrucks = (params) => {
  return useQuery({
    queryKey: ['trucks', params],
    queryFn: () => truckCoreService.getTrucks(params),
  });
};

export const useTruck = (truckId) => {
  return useQuery({
    queryKey: ['truck', truckId],
    queryFn: () => truckCoreService.getTruckById(truckId),
    enabled: !!truckId,
  });
};

export const useUpdateTruck = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ truckId, data }) => truckCoreService.updateTruck(truckId, data),
    onSuccess: (_, { truckId }) => {
      toast.success('Truck updated successfully');
      queryClient.invalidateQueries({ queryKey: ['truck', truckId] });
      queryClient.invalidateQueries({ queryKey: ['trucks'] });
    },
    onError: () => toast.error('Failed to update truck')
  });
};

export const useDeleteTruck = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (truckId) => truckCoreService.deleteTruck(truckId),
    onSuccess: () => {
      toast.success('Truck deleted successfully');
      queryClient.invalidateQueries({ queryKey: ['trucks'] });
    },
    onError: () => toast.error('Failed to delete truck')
  });
};
