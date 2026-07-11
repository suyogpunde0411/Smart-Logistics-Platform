import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { businessShipmentService } from '../services/businessShipmentService';
import toast from 'react-hot-toast';

export const useShipments = (params) => {
  return useQuery({
    queryKey: ['businessShipments', params],
    queryFn: () => businessShipmentService.getShipments(params),
  });
};

export const useShipmentDetails = (id) => {
  return useQuery({
    queryKey: ['businessShipment', id],
    queryFn: () => businessShipmentService.getShipmentDetails(id),
    enabled: !!id,
  });
};

export const useCreateShipment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: businessShipmentService.createShipment,
    onSuccess: () => {
      toast.success('Shipment created successfully');
      queryClient.invalidateQueries({ queryKey: ['businessShipments'] });
    },
    onError: (err) => toast.error(err.response?.data?.message || 'Failed to create shipment'),
  });
};
