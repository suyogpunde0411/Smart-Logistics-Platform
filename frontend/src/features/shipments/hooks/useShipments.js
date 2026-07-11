import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { shipmentCoreService } from '../services/shipmentCoreService';
import toast from 'react-hot-toast';

export const useShipments = (params) => {
  return useQuery({
    queryKey: ['shipments', params],
    queryFn: () => shipmentCoreService.getShipments(params),
  });
};

export const useShipment = (id) => {
  return useQuery({
    queryKey: ['shipment', id],
    queryFn: () => shipmentCoreService.getShipmentById(id),
    enabled: !!id,
  });
};

export const useUpdateShipmentStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, status }) => shipmentCoreService.updateShipmentStatus(id, status),
    onSuccess: (_, { id }) => {
      toast.success('Shipment status updated');
      queryClient.invalidateQueries({ queryKey: ['shipment', id] });
      queryClient.invalidateQueries({ queryKey: ['shipments'] });
    },
    onError: () => toast.error('Failed to update shipment status')
  });
};

export const useDeleteShipment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id) => shipmentCoreService.deleteShipment(id),
    onSuccess: () => {
      toast.success('Shipment deleted successfully');
      queryClient.invalidateQueries({ queryKey: ['shipments'] });
    },
    onError: () => toast.error('Failed to delete shipment')
  });
};
