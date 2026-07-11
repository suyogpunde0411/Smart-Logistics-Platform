import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { bidManagementService } from '../services/bidManagementService';
import toast from 'react-hot-toast';

export const useSubmitBid = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ shipmentId, bidData }) => bidManagementService.submitBid(shipmentId, bidData),
    onSuccess: (_, { shipmentId }) => {
      toast.success('Bid submitted successfully!');
      queryClient.invalidateQueries({ queryKey: ['bids', shipmentId] });
      queryClient.invalidateQueries({ queryKey: ['myBids'] });
    },
    onError: () => toast.error('Failed to submit bid.')
  });
};

export const useWithdrawBid = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ shipmentId, bidId }) => bidManagementService.withdrawBid(shipmentId, bidId),
    onSuccess: (_, { shipmentId }) => {
      toast.success('Bid withdrawn.');
      queryClient.invalidateQueries({ queryKey: ['bids', shipmentId] });
      queryClient.invalidateQueries({ queryKey: ['myBids'] });
    },
    onError: () => toast.error('Failed to withdraw bid.')
  });
};

export const useShipmentBids = (shipmentId) => {
  return useQuery({
    queryKey: ['bids', shipmentId],
    queryFn: () => bidManagementService.getBidsForShipment(shipmentId),
    enabled: !!shipmentId,
    refetchInterval: 30000 // Poll for new bids every 30s
  });
};
