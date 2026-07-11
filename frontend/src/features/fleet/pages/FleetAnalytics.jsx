import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ChartCard } from '@/features/dashboard/components/ChartCard';
import { useFleetAnalytics } from '../hooks/useFleetAnalytics';

export const FleetAnalytics = () => {
  const { data, isLoading } = useFleetAnalytics();

  const utilData = [
    { name: 'Jan', value: 65 }, { name: 'Feb', value: 70 }, { name: 'Mar', value: 80 },
    { name: 'Apr', value: 85 }, { name: 'May', value: 92 }, { name: 'Jun', value: 95 }
  ];

  const maintCostData = [
    { name: 'Jan', value: 4500 }, { name: 'Feb', value: 3200 }, { name: 'Mar', value: 5000 },
    { name: 'Apr', value: 2200 }, { name: 'May', value: 8500 }, { name: 'Jun', value: 1800 }
  ];

  return (
    <PageContainer>
      <DashboardHeader title="Fleet Analytics" description="Utilization, efficiency, and maintenance cost metrics." />
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        <ChartCard 
          title="Fleet Utilization (%)" 
          data={utilData} 
          config={[{ dataKey: 'value', stroke: '#10b981', fill: '#10b981' }]} 
          type="area" 
        />
        <ChartCard 
          title="Maintenance Cost (USD)" 
          data={maintCostData} 
          config={[{ dataKey: 'value', stroke: '#ef4444', fill: '#ef4444' }]} 
          type="bar" 
        />
      </div>
    </PageContainer>
  );
};
