import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { DriverCard } from '../components/DriverCard';
import { useFleetDrivers } from '../hooks/useFleetDrivers';
import { Plus } from 'lucide-react';

export const DriverManagement = () => {
  const { data: drivers, isLoading } = useFleetDrivers();

  return (
    <PageContainer>
      <div className="flex justify-between items-end mb-6">
        <DashboardHeader title="Driver Management" description="Manage driver roster, assignments, and performance." />
        <button className="bg-primary text-primary-foreground px-4 py-2 rounded-md font-medium text-sm flex items-center gap-2 hover:bg-primary/90">
          <Plus className="w-4 h-4" /> Onboard Driver
        </button>
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading drivers...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {drivers?.length ? drivers.map(d => (
            <DriverCard key={d.id} driver={d} />
          )) : (
            <div className="col-span-full p-8 text-center border border-dashed rounded-xl text-muted-foreground">
              No drivers onboarded yet.
            </div>
          )}
        </div>
      )}
    </PageContainer>
  );
};
