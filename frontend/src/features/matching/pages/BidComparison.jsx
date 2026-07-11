import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { BidComparisonTable } from '../components/BidComparisonTable';
import { useShipmentBids } from '../hooks/useBidManagement';
import { ArrowLeft } from 'lucide-react';

export const BidComparison = () => {
  const { shipmentId } = useParams();
  const navigate = useNavigate();
  
  const { data: bids, isLoading } = useShipmentBids(shipmentId);

  // Mock data if no real backend data
  const mockBids = bids || [
    { id: 'BID-1', fleetOwnerName: 'Express Logistics', rating: 4.8, isVerified: true, truckType: 'Container 20ft', truckRegistration: 'MH 04 AB 1234', driverName: 'Rahul T', amount: 42000, estimatedPickup: 'Nov 20, 08:00 AM' },
    { id: 'BID-2', fleetOwnerName: 'Speed Cargo', rating: 4.5, isVerified: false, truckType: 'Container 20ft', truckRegistration: 'DL 01 CD 5678', driverName: 'Suresh M', amount: 46000, estimatedPickup: 'Nov 20, 11:00 AM' },
    { id: 'BID-3', fleetOwnerName: 'Blue Dart Freight', rating: 4.9, isVerified: true, truckType: 'Flatbed', truckRegistration: 'KA 05 XY 9012', driverName: 'Vikram S', amount: 41500, estimatedPickup: 'Nov 21, 09:00 AM' }
  ];

  return (
    <PageContainer>
      <div className="mb-6">
        <button 
          onClick={() => navigate(`/shipments/${shipmentId}`)}
          className="flex items-center gap-2 text-sm font-medium text-muted-foreground hover:text-foreground mb-4 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Back to Shipment Details
        </button>
        <DashboardHeader 
          title="Bid Comparison" 
          description={`Review and accept offers from fleet owners for Shipment ${shipmentId}.`} 
        />
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading bids...</div>
      ) : (
        <div className="animate-in fade-in slide-in-from-bottom-4">
          <BidComparisonTable shipmentId={shipmentId} bids={mockBids} />
        </div>
      )}
    </PageContainer>
  );
};
