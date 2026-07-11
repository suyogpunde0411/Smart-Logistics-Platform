import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { shipmentMatchService } from '../services/shipmentMatchService';
import toast from 'react-hot-toast';

export const useShipmentMatches = (shipmentId) => {
  return useQuery({
    queryKey: ['shipmentMatches', shipmentId],
    queryFn: () => shipmentMatchService.getRecommendedMatches(shipmentId),
    enabled: !!shipmentId,
  });
};

export const useShipmentBids = (shipmentId) => {
  return useQuery({
    queryKey: ['shipmentBids', shipmentId],
    queryFn: () => shipmentMatchService.getBids(shipmentId),
    enabled: !!shipmentId,
  });
};

export const useAcceptBid = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ shipmentId, bidId }) => shipmentMatchService.acceptBid(shipmentId, bidId),
    onSuccess: (_, { shipmentId }) => {
      toast.success('Bid accepted successfully!');
      queryClient.invalidateQueries({ queryKey: ['shipmentBids', shipmentId] });
      queryClient.invalidateQueries({ queryKey: ['shipment', shipmentId] });
    },
    onError: () => toast.error('Failed to accept bid')
  });
};
