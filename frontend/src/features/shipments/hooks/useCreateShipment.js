import { useMutation, useQueryClient } from '@tanstack/react-query';
import { shipmentCreationService } from '../services/shipmentCreationService';
import toast from 'react-hot-toast';

export const useCreateShipment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (shipmentData) => shipmentCreationService.createShipment(shipmentData),
    onSuccess: () => {
      toast.success('Shipment published successfully!');
      queryClient.invalidateQueries({ queryKey: ['shipments'] });
    },
    onError: () => toast.error('Failed to create shipment. Please try again.')
  });
};
