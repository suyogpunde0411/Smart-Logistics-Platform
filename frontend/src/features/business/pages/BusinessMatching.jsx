import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { BidCard } from '../components/BidCard';
import { useBusinessMatches, useAcceptBid } from '../hooks/useBusinessMatches';

export const BusinessMatching = () => {
  // Hardcode a shipment ID for demo, usually we'd pass this or list all matches grouped by shipment
  const { data: bids, isLoading } = useBusinessMatches(1); 
  const { mutate: acceptBid } = useAcceptBid();

  return (
    <PageContainer>
      <DashboardHeader title="Shipment Matching & Bids" description="Review driver bids and match scores to select the best carrier." />
      
      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading bids...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
          {bids?.length ? bids.map(bid => (
            <BidCard 
              key={bid.id} 
              bid={bid} 
              onAccept={(id) => acceptBid(id)} 
              onReject={(id) => console.log('Reject', id)} 
            />
          )) : (
            <div className="col-span-full p-8 bg-card border border-border rounded-xl text-center text-muted-foreground">
              No bids received yet.
            </div>
          )}
        </div>
      )}
    </PageContainer>
  );
};
