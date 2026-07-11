import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useDriverProfile } from '../hooks/useDriverProfile';

export const DriverProfile = () => {
  const { data, isLoading } = useDriverProfile();

  if (isLoading) return <div>Loading...</div>;

  return (
    <PageContainer>
      <DashboardHeader title="My Profile" description="Manage your personal and license information" />
      <div className="bg-card rounded-xl p-6 border border-border shadow-sm">
        <h3 className="text-lg font-medium mb-4">Personal Details</h3>
        <p>Email: {data?.email || 'driver@example.com'}</p>
        {/* Placeholder for full profile form */}
      </div>
    </PageContainer>
  );
};
