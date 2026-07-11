import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { MapCardPlaceholder } from '@/features/dashboard/components/MapCardPlaceholder';

export const LiveTracking = () => {
  return (
    <PageContainer className="h-full flex flex-col">
      <DashboardHeader title="Live Tracking" description="View your current route and ETA" />
      <div className="flex-1 min-h-[500px]">
        <MapCardPlaceholder title="Current Route" height="100%" />
      </div>
    </PageContainer>
  );
};
