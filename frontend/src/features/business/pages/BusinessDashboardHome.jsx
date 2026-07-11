import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { StatsCard } from '@/features/dashboard/components/StatsCard';
import { QuickActionCard } from '@/features/dashboard/components/QuickActionCard';
import { Package, Truck, DollarSign, Network, FilePlus, Eye, MapPin } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const BusinessDashboardHome = () => {
  const navigate = useNavigate();

  return (
    <PageContainer>
      <DashboardHeader title="Business Dashboard" description="Overview of your logistics operations." />
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatsCard title="Active Shipments" value="12" icon={Package} trend={5} />
        <StatsCard title="Pending Matches" value="3" icon={Network} />
        <StatsCard title="Completed (This Month)" value="45" icon={Truck} trend={12} />
        <StatsCard title="Monthly Spend" value="$12,450" icon={DollarSign} />
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <QuickActionCard title="Create Shipment" icon={FilePlus} onClick={() => navigate('/shipments/new')} />
        <QuickActionCard title="My Shipments" icon={Eye} onClick={() => navigate('/shipments')} />
        <QuickActionCard title="Matching" icon={Network} onClick={() => navigate('/matching')} />
        <QuickActionCard title="Tracking" icon={MapPin} onClick={() => navigate('/tracking')} />
      </div>
    </PageContainer>
  );
};
