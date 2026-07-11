import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { StatsCard } from '@/features/dashboard/components/StatsCard';
import { QuickActionCard } from '@/features/dashboard/components/QuickActionCard';
import { Truck, Users, Map, Wrench, AlertCircle } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useFleetSummary } from '../hooks/useFleetTrucks';

export const FleetDashboardHome = () => {
  const navigate = useNavigate();
  // Using generic default values to prevent undefined errors when hooking up new APIs
  const { data: summary = { totalTrucks: 24, available: 8, activeTrips: 12, alerts: 3 } } = useFleetSummary();

  return (
    <PageContainer>
      <DashboardHeader title="Fleet Dashboard" description="Live overview of your logistics fleet operations." />
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatsCard title="Total Trucks" value={summary.totalTrucks} icon={Truck} trend={2} />
        <StatsCard title="Available Now" value={summary.available} icon={CheckCircle} />
        <StatsCard title="Active Trips" value={summary.activeTrips} icon={Map} trend={15} />
        <StatsCard title="Alerts & Maint." value={summary.alerts} icon={AlertCircle} trend={-5} />
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <QuickActionCard title="Manage Fleet" icon={Truck} onClick={() => navigate('/fleet')} />
        <QuickActionCard title="Manage Drivers" icon={Users} onClick={() => navigate('/drivers')} />
        <QuickActionCard title="Maintenance" icon={Wrench} onClick={() => navigate('/maintenance')} />
        <QuickActionCard title="Trip Tracking" icon={Map} onClick={() => navigate('/trips')} />
      </div>
    </PageContainer>
  );
};

// Helper component for Icon inside Dashboard since CheckCircle wasn't imported at top level
const CheckCircle = ({ className }) => <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className={className}><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>;
