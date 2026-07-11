import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';

export const AdminTrucks = () => {
  return (
    <PageContainer>
      <DashboardHeader title="Global Truck Directory" description="Read-only view of all registered trucks on the platform." />
      <div className="p-8 text-center border border-dashed rounded-xl text-muted-foreground mt-6">
        Master Truck Table placeholder. (Admin view-only mode)
      </div>
    </PageContainer>
  );
};
