import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { driverShipmentService } from '../services/driverShipmentService';
import toast from 'react-hot-toast';

export const useAvailableShipments = (params) => {
  return useQuery({
    queryKey: ['availableShipments', params],
    queryFn: () => driverShipmentService.getAvailableShipments(params),
  });
};

export const useDriverMatches = () => {
  return useQuery({
    queryKey: ['driverMatches'],
    queryFn: driverShipmentService.getMyMatches,
  });
};

export const useRequestMatch = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ shipmentId, bidAmount }) => driverShipmentService.requestMatch(shipmentId, bidAmount),
    onSuccess: () => {
      toast.success('Match requested successfully');
      queryClient.invalidateQueries({ queryKey: ['driverMatches'] });
      queryClient.invalidateQueries({ queryKey: ['availableShipments'] });
    },
    onError: () => toast.error('Failed to request match'),
  });
};
