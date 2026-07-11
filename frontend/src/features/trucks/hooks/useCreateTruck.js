import { useMutation, useQueryClient } from '@tanstack/react-query';
import { truckRegistrationService } from '../services/truckRegistrationService';
import toast from 'react-hot-toast';

export const useCreateTruck = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (truckData) => truckRegistrationService.createTruck(truckData),
    onSuccess: () => {
      toast.success('Truck registered successfully!');
      queryClient.invalidateQueries({ queryKey: ['trucks'] });
    },
    onError: () => toast.error('Failed to register truck. Please try again.')
  });
};
