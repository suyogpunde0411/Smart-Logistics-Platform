import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { TripCard } from '../components/TripCard';
import { useDriverTrips, useUpdateTripStatus } from '../hooks/useDriverTrips';

export const DriverTrips = () => {
  const { data, isLoading } = useDriverTrips();
  const { mutate: updateStatus } = useUpdateTripStatus();

  return (
    <PageContainer>
      <DashboardHeader title="My Trips" description="Manage your active and completed trips" />
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {data?.map(trip => (
          <TripCard key={trip.id} trip={trip} onUpdateStatus={(id, status) => updateStatus({ tripId: id, status })} />
        ))}
      </div>
    </PageContainer>
  );
};
