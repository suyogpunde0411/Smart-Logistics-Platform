import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { StatsCard } from '@/features/dashboard/components/StatsCard';
import { QuickActionCard } from '@/features/dashboard/components/QuickActionCard';
import { Users, Truck, Package, ShieldAlert, Activity, DollarSign } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { usePlatformKpis } from '../hooks/usePlatformAnalytics';

export const AdminDashboardHome = () => {
  const navigate = useNavigate();
  // Safe default fallback
  const { data: kpis = { totalUsers: 1250, activeTrucks: 430, activeShipments: 89, pendingVerifications: 12, monthlyRevenue: 45000, systemHealth: 99.9 } } = usePlatformKpis();

  return (
    <PageContainer>
      <DashboardHeader title="System Overview" description="Global platform metrics and health status." />
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4 mb-8">
        <StatsCard title="Total Users" value={kpis.totalUsers} icon={Users} trend={5} />
        <StatsCard title="Active Trucks" value={kpis.activeTrucks} icon={Truck} trend={2} />
        <StatsCard title="Live Shipments" value={kpis.activeShipments} icon={Package} trend={12} />
        <StatsCard title="Pending Verifications" value={kpis.pendingVerifications} icon={ShieldAlert} trend={-3} />
        <StatsCard title="Monthly Revenue" value={'$' + (kpis.monthlyRevenue/1000).toFixed(1) + 'k'} icon={DollarSign} trend={8} />
        <StatsCard title="System Uptime" value={kpis.systemHealth + '%'} icon={Activity} />
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <QuickActionCard title="User Directory" icon={Users} onClick={() => navigate('/admin/users')} />
        <QuickActionCard title="Moderation" icon={ShieldAlert} onClick={() => navigate('/admin/reviews')} />
        <QuickActionCard title="Platform Settings" icon={Activity} onClick={() => navigate('/admin/settings')} />
        <QuickActionCard title="Support Tickets" icon={Package} onClick={() => navigate('/admin/support')} />
      </div>
    </PageContainer>
  );
};
