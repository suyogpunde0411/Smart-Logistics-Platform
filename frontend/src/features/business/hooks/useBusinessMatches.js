import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { businessMatchingService } from '../services/businessMatchingService';
import toast from 'react-hot-toast';

export const useBusinessMatches = (shipmentId) => {
  return useQuery({
    queryKey: ['businessMatches', shipmentId],
    queryFn: () => businessMatchingService.getMatchesForShipment(shipmentId),
    enabled: !!shipmentId,
  });
};

export const useAcceptBid = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: businessMatchingService.acceptBid,
    onSuccess: () => {
      toast.success('Bid accepted successfully');
      queryClient.invalidateQueries({ queryKey: ['businessMatches'] });
      queryClient.invalidateQueries({ queryKey: ['businessShipments'] });
    },
    onError: () => toast.error('Failed to accept bid'),
  });
};
