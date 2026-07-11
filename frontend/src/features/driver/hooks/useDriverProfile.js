import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { driverProfileService } from '../services/driverProfileService';
import toast from 'react-hot-toast';

export const useDriverProfile = () => {
  return useQuery({
    queryKey: ['driverProfile'],
    queryFn: driverProfileService.getProfile,
  });
};

export const useUpdateAvailability = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: driverProfileService.updateAvailability,
    onSuccess: () => {
      toast.success('Availability updated');
      queryClient.invalidateQueries({ queryKey: ['driverProfile'] });
    },
    onError: () => toast.error('Failed to update availability'),
  });
};
