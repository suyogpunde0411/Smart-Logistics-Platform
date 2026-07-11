import React, { useState } from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { SupportTicketTable } from '../components/SupportTicketTable';
import { useSupportTickets } from '../hooks/useSupportTickets';

export const SupportTickets = () => {
  const [filter, setFilter] = useState('OPEN');
  const { data: tickets, isLoading } = useSupportTickets({ status: filter });

  // Mock data for display if backend is empty
  const mockTickets = tickets?.length ? tickets : [
    { id: 'TKT-991283', subject: 'Cannot verify my GST document', submittedBy: 'user@business.com', status: filter },
    { id: 'TKT-991284', subject: 'Truck not appearing in search', submittedBy: 'owner@fleet.com', status: filter }
  ];

  return (
    <PageContainer>
      <DashboardHeader title="Support Tickets" description="Manage user inquiries and platform issues." />
      
      <div className="flex gap-2 mb-6">
        {['OPEN', 'RESOLVED'].map(tab => (
          <button 
            key={tab}
            onClick={() => setFilter(tab)}
            className={"px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-colors " + (filter === tab ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground hover:bg-muted/80')}
          >
            {tab}
          </button>
        ))}
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading support tickets...</div>
      ) : (
        <SupportTicketTable tickets={mockTickets} onResolve={(id) => console.log('Resolved', id)} />
      )}
    </PageContainer>
  );
};
