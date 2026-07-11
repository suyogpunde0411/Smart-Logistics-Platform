import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { FeatureFlagPanel } from '../components/FeatureFlagPanel';
import { useSystemSettings, useUpdateSetting } from '../hooks/useSystemSettings';

export const SystemSettings = () => {
  const { data: settings, isLoading } = useSystemSettings();
  const { mutate: updateSetting } = useUpdateSetting();

  const handleToggle = (key, value) => {
    updateSetting({ key, value });
  };

  // Mock settings if API returns nothing
  const activeSettings = settings || {
    MAINTENANCE_MODE: false,
    ALLOW_REGISTRATION: true,
    AUTO_MATCHING: true
  };

  return (
    <PageContainer>
      <DashboardHeader title="System Settings" description="Global platform configurations and feature flags." />
      
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        {isLoading ? (
          <div className="p-8 text-center animate-pulse text-muted-foreground">Loading configurations...</div>
        ) : (
          <FeatureFlagPanel settings={activeSettings} onToggle={handleToggle} />
        )}
      </div>
    </PageContainer>
  );
};
