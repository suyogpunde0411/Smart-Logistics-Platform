import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useDriverTruck } from '../hooks/useDriverTruck';

export const DriverTruck = () => {
  const { data, isLoading } = useDriverTruck();

  return (
    <PageContainer>
      <DashboardHeader title="My Truck" description="View and manage your assigned vehicle" />
      <div className="bg-card rounded-xl p-6 border border-border shadow-sm">
        <h3 className="text-lg font-medium mb-4">Truck Information</h3>
        <p>Status: {data?.status || 'Active'}</p>
      </div>
    </PageContainer>
  );
};
