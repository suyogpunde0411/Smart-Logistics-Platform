import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { MaintenanceCard } from '../components/MaintenanceCard';
import { useFleetMaintenance } from '../hooks/useFleetMaintenance';
import { Wrench } from 'lucide-react';

export const MaintenanceSchedule = () => {
  const { data: logs, isLoading } = useFleetMaintenance();

  // Mocking some data if API is empty
  const mockLogs = logs?.length ? logs : [
    { id: 1, truckNumber: 'TRK-901', serviceType: 'Engine Oil Change', dueDate: '2026-07-01' },
    { id: 2, truckNumber: 'TRK-442', serviceType: 'Brake Pad Replacement', dueDate: '2026-07-15' },
    { id: 3, truckNumber: 'TRK-118', serviceType: 'Annual Inspection', dueDate: '2026-08-05' }
  ];

  return (
    <PageContainer>
      <div className="flex justify-between items-end mb-6">
        <DashboardHeader title="Maintenance & Services" description="Track scheduled maintenance and active alerts." />
        <button className="bg-primary text-primary-foreground px-4 py-2 rounded-md font-medium text-sm flex items-center gap-2 hover:bg-primary/90">
          <Wrench className="w-4 h-4" /> Log Service
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {mockLogs.map(log => (
          <MaintenanceCard key={log.id} log={log} />
        ))}
      </div>
    </PageContainer>
  );
};
