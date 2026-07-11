import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ChartCard } from '@/features/dashboard/components/ChartCard';
import { usePlatformKpis } from '../hooks/usePlatformAnalytics';

export const PlatformAnalytics = () => {
  const { data, isLoading } = usePlatformKpis();

  const userGrowthData = [
    { name: 'Jan', value: 120 }, { name: 'Feb', value: 250 }, { name: 'Mar', value: 480 },
    { name: 'Apr', value: 890 }, { name: 'May', value: 1250 }
  ];

  const revenueData = [
    { name: 'Jan', value: 15000 }, { name: 'Feb', value: 18000 }, { name: 'Mar', value: 24000 },
    { name: 'Apr', value: 32000 }, { name: 'May', value: 45000 }
  ];

  return (
    <PageContainer>
      <DashboardHeader title="Platform Analytics" description="Global growth, engagement, and revenue metrics." />
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        <ChartCard 
          title="Active Users Growth" 
          data={userGrowthData} 
          config={[{ dataKey: 'value', stroke: '#3b82f6', fill: '#3b82f6' }]} 
          type="area" 
        />
        <ChartCard 
          title="Platform Revenue (USD)" 
          data={revenueData} 
          config={[{ dataKey: 'value', stroke: '#10b981', fill: '#10b981' }]} 
          type="bar" 
        />
      </div>
    </PageContainer>
  );
};
