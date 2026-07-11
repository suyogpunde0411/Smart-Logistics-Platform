import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { MapCardPlaceholder } from '@/features/dashboard/components/MapCardPlaceholder';
import { Timeline } from '@/features/dashboard/components/Timeline';

export const BusinessTracking = () => {
  const events = [
    { title: 'Shipment Created', date: '2026-07-10 10:00 AM' },
    { title: 'Driver Assigned (John Doe)', date: '2026-07-10 02:30 PM' },
    { title: 'Picked Up', date: '2026-07-11 08:00 AM', description: 'Driver confirmed pickup from warehouse.' },
    { title: 'In Transit', date: '2026-07-11 09:15 AM' }
  ];

  return (
    <PageContainer className="h-full flex flex-col">
      <DashboardHeader title="Live Tracking" description="Monitor active shipments and delivery ETA." />
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-1">
        <div className="lg:col-span-2 min-h-[500px] border border-border rounded-xl overflow-hidden shadow-sm">
          <MapCardPlaceholder title="Active Fleet Route" height="100%" />
        </div>
        <div className="bg-card p-6 border border-border rounded-xl shadow-sm">
          <h3 className="text-lg font-semibold mb-6">Shipment Timeline</h3>
          <Timeline items={events} />
        </div>
      </div>
    </PageContainer>
  );
};
