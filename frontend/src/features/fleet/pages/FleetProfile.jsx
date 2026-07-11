import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';

export const FleetProfile = () => {
  return (
    <PageContainer>
      <DashboardHeader title="Fleet Owner Profile" description="Manage your company and operational details." />
      <div className="bg-card rounded-xl p-8 border border-border shadow-sm max-w-3xl">
        <h3 className="text-lg font-semibold mb-6">Company Information</h3>
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-muted-foreground mb-1">Company Name</label>
              <p className="font-medium">Prime Transport Solutions</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-muted-foreground mb-1">Operating License</label>
              <p className="font-medium">OP-9988776655</p>
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-muted-foreground mb-1">HQ Address</label>
            <p className="font-medium">456 Transport Hub, Logistics Park, 90210</p>
          </div>
        </div>
      </div>
    </PageContainer>
  );
};
