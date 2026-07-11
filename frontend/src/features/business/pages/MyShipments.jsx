import React, { useState } from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ShipmentTable } from '../components/ShipmentTable';
import { useShipments } from '../hooks/useShipments';
import { Plus } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const MyShipments = () => {
  const [filter, setFilter] = useState('ALL');
  const { data: shipments, isLoading } = useShipments({ status: filter === 'ALL' ? undefined : filter });
  const navigate = useNavigate();

  const tabs = ['ALL', 'DRAFT', 'PENDING', 'ACTIVE', 'COMPLETED', 'CANCELLED'];

  return (
    <PageContainer>
      <div className="flex justify-between items-end mb-6">
        <DashboardHeader title="My Shipments" description="Manage all your logistics requests" />
        <button 
          onClick={() => navigate('/shipments/new')}
          className="bg-primary text-primary-foreground px-4 py-2 rounded-md font-medium text-sm flex items-center gap-2 hover:bg-primary/90"
        >
          <Plus className="w-4 h-4" /> New Shipment
        </button>
      </div>

      <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
        {tabs.map(tab => (
          <button 
            key={tab}
            onClick={() => setFilter(tab)}
            className={`px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-colors ${filter === tab ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground hover:bg-muted/80'}`}
          >
            {tab}
          </button>
        ))}
      </div>

      <ShipmentTable shipments={shipments || []} isLoading={isLoading} />
    </PageContainer>
  );
};
