import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { truckInsuranceService } from '../services/truckInsuranceService';
import toast from 'react-hot-toast';

export const useTruckInsurance = (truckId) => {
  return useQuery({
    queryKey: ['truckInsurance', truckId],
    queryFn: () => truckInsuranceService.getInsuranceDetails(truckId),
    enabled: !!truckId,
  });
};

export const useUpdateInsurance = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ truckId, data }) => truckInsuranceService.updateInsurance(truckId, data),
    onSuccess: (_, { truckId }) => {
      toast.success('Insurance records updated');
      queryClient.invalidateQueries({ queryKey: ['truckInsurance', truckId] });
    },
    onError: () => toast.error('Failed to update insurance')
  });
};
