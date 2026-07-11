import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';

export const BusinessProfile = () => {
  return (
    <PageContainer>
      <DashboardHeader title="Business Profile" description="Manage your company details and GST configuration." />
      <div className="bg-card rounded-xl p-8 border border-border shadow-sm max-w-3xl">
        <h3 className="text-lg font-semibold mb-6">Company Information</h3>
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-muted-foreground mb-1">Company Name</label>
              <p className="font-medium">Acme Logistics Corp</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-muted-foreground mb-1">GST/Tax ID</label>
              <p className="font-medium">GSTIN123456789</p>
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-muted-foreground mb-1">Billing Address</label>
            <p className="font-medium">123 Industrial Way, Metro City, 90210</p>
          </div>
        </div>
      </div>
    </PageContainer>
  );
};
