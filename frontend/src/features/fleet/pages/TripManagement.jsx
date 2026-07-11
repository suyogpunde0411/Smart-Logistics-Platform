import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { MapCardPlaceholder } from '@/features/dashboard/components/MapCardPlaceholder';
import { Timeline } from '@/features/dashboard/components/Timeline';
import { useFleetTrips } from '../hooks/useFleetTrips';

export const TripManagement = () => {
  const { data: trips, isLoading } = useFleetTrips();

  const mockTimeline = [
    { title: 'Trip Assigned', date: '2026-07-11 08:00 AM' },
    { title: 'Departed Warehouse', date: '2026-07-11 09:30 AM' },
    { title: 'In Transit - Highway 42', date: '2026-07-11 11:45 AM' }
  ];

  return (
    <PageContainer className="h-full flex flex-col">
      <DashboardHeader title="Live Trip Tracking" description="Monitor active shipments and delivery ETAs across the fleet." />
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-1 mt-6">
        <div className="lg:col-span-2 min-h-[500px] border border-border rounded-xl overflow-hidden shadow-sm">
          <MapCardPlaceholder title="Active Fleet Routes" height="100%" />
        </div>
        <div className="bg-card p-6 border border-border rounded-xl shadow-sm overflow-y-auto">
          <h3 className="text-lg font-semibold mb-6">Recent Trip Events</h3>
          <Timeline items={mockTimeline} />
        </div>
      </div>
    </PageContainer>
  );
};
