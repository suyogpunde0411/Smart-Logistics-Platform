import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { driverTripService } from '../services/driverTripService';
import toast from 'react-hot-toast';

export const useDriverTrips = () => {
  return useQuery({
    queryKey: ['driverTrips'],
    queryFn: driverTripService.getMyTrips,
  });
};

export const useUpdateTripStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ tripId, status }) => driverTripService.updateTripStatus(tripId, status),
    onSuccess: () => {
      toast.success('Trip status updated');
      queryClient.invalidateQueries({ queryKey: ['driverTrips'] });
    },
    onError: () => toast.error('Failed to update trip status'),
  });
};
