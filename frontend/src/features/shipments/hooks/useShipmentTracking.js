import { useQuery } from '@tanstack/react-query';
import { shipmentTrackingService } from '../services/shipmentTrackingService';

export const useShipmentTimeline = (shipmentId) => {
  return useQuery({
    queryKey: ['shipmentTimeline', shipmentId],
    queryFn: () => shipmentTrackingService.getTimeline(shipmentId),
    enabled: !!shipmentId,
  });
};

export const useShipmentTracking = (shipmentId) => {
  return useQuery({
    queryKey: ['shipmentTracking', shipmentId],
    queryFn: () => shipmentTrackingService.getLiveTracking(shipmentId),
    enabled: !!shipmentId,
    refetchInterval: 30000 // Refetch every 30 seconds for live tracking
  });
};
