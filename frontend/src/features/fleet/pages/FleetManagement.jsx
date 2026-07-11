import React, { useState } from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { TruckCard } from '../components/TruckCard';
import { useFleetTrucks } from '../hooks/useFleetTrucks';
import { Plus } from 'lucide-react';

export const FleetManagement = () => {
  const [filter, setFilter] = useState('ALL');
  const { data: trucks, isLoading } = useFleetTrucks({ status: filter === 'ALL' ? undefined : filter });
  const tabs = ['ALL', 'AVAILABLE', 'IN_TRANSIT', 'MAINTENANCE'];

  return (
    <PageContainer>
      <div className="flex justify-between items-end mb-6">
        <DashboardHeader title="Fleet Management" description="Manage all vehicles, assignments, and utilization." />
        <button className="bg-primary text-primary-foreground px-4 py-2 rounded-md font-medium text-sm flex items-center gap-2 hover:bg-primary/90">
          <Plus className="w-4 h-4" /> Add Truck
        </button>
      </div>

      <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
        {tabs.map(tab => (
          <button 
            key={tab}
            onClick={() => setFilter(tab)}
            className={"px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-colors " + (filter === tab ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground hover:bg-muted/80')}
          >
            {tab}
          </button>
        ))}
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading fleet data...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {trucks?.length ? trucks.map(t => (
            <TruckCard key={t.id} truck={t} />
          )) : (
            <div className="col-span-full p-8 text-center border border-dashed rounded-xl text-muted-foreground">
              No trucks found for this filter.
            </div>
          )}
        </div>
      )}
    </PageContainer>
  );
};
