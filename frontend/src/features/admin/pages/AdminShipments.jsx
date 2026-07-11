import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';

export const AdminShipments = () => {
  return (
    <PageContainer>
      <DashboardHeader title="Global Shipment Ledger" description="Read-only view of all freight movements." />
      <div className="p-8 text-center border border-dashed rounded-xl text-muted-foreground mt-6">
        Master Shipment Table placeholder. (Admin view-only mode)
      </div>
    </PageContainer>
  );
};
