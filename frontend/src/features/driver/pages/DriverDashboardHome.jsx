import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { StatsCard } from '@/features/dashboard/components/StatsCard';
import { QuickActionCard } from '@/features/dashboard/components/QuickActionCard';
import { NotificationWidget } from '@/features/dashboard/components/NotificationWidget';
import { ChartCard } from '@/features/dashboard/components/ChartCard';
import { Map, Truck, DollarSign, Package, CheckCircle, User } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const DriverDashboardHome = () => {
  const navigate = useNavigate();
  // We'll mock the hook for now to ensure rendering
  const { data: stats, isLoading } = { data: { earnings: 1250, completed: 42, activeTrips: 1, rating: 4.8 }, isLoading: false };
  
  if (isLoading) return <div>Loading dashboard...</div>;

  const earningData = [
    { name: 'Mon', value: 150 }, { name: 'Tue', value: 230 }, { name: 'Wed', value: 180 },
    { name: 'Thu', value: 290 }, { name: 'Fri', value: 200 }, { name: 'Sat', value: 200 }, { name: 'Sun', value: 0 }
  ];

  return (
    <PageContainer>
      <DashboardHeader 
        title="Driver Dashboard" 
        description="Welcome back! Here's your overview for today." 
      />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
        <StatsCard title="Monthly Earnings" value={`$` + stats.earnings} icon={DollarSign} trend={12} description="vs last month" />
        <StatsCard title="Completed Trips" value={stats.completed} icon={CheckCircle} trend={5} description="vs last month" />
        <StatsCard title="Active Trips" value={stats.activeTrips} icon={Map} />
        <StatsCard title="Driver Rating" value={stats.rating} icon={User} />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <ChartCard 
            title="Weekly Earnings" 
            data={earningData} 
            config={[{ dataKey: 'value', stroke: '#10b981', fill: '#10b981' }]} 
            type="bar" 
          />
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <QuickActionCard title="Find Shipments" icon={Package} onClick={() => navigate('/shipments/available')} />
            <QuickActionCard title="My Trips" icon={Map} onClick={() => navigate('/trips')} />
            <QuickActionCard title="My Truck" icon={Truck} onClick={() => navigate('/truck/me')} />
            <QuickActionCard title="Live Tracking" icon={Map} onClick={() => navigate('/tracking')} />
          </div>
        </div>
        <div>
          <NotificationWidget notifications={[{ title: 'New Shipment Available', time: '10m ago' }]} />
        </div>
      </div>
    </PageContainer>
  );
};
