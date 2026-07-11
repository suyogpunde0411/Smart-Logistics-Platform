import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ChartCard } from '@/features/dashboard/components/ChartCard';
import { useBusinessAnalytics } from '../hooks/useBusinessAnalytics';

export const BusinessAnalytics = () => {
  const { data, isLoading } = useBusinessAnalytics();

  const volumeData = [
    { name: 'Jan', value: 40 }, { name: 'Feb', value: 30 }, { name: 'Mar', value: 65 },
    { name: 'Apr', value: 85 }, { name: 'May', value: 90 }, { name: 'Jun', value: 110 }
  ];

  const costData = [
    { name: 'Jan', value: 4000 }, { name: 'Feb', value: 3200 }, { name: 'Mar', value: 6000 },
    { name: 'Apr', value: 8200 }, { name: 'May', value: 8500 }, { name: 'Jun', value: 9800 }
  ];

  return (
    <PageContainer>
      <DashboardHeader title="Analytics & Reports" description="Dive deep into your logistics performance and spending." />
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        <ChartCard 
          title="Shipment Volume" 
          data={volumeData} 
          config={[{ dataKey: 'value', stroke: '#3b82f6', fill: '#3b82f6' }]} 
          type="area" 
        />
        <ChartCard 
          title="Monthly Spend (USD)" 
          data={costData} 
          config={[{ dataKey: 'value', stroke: '#f59e0b', fill: '#f59e0b' }]} 
          type="bar" 
        />
      </div>
    </PageContainer>
  );
};
