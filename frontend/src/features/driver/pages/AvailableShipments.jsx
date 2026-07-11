import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ShipmentCard } from '../components/ShipmentCard';
import { useAvailableShipments, useRequestMatch } from '../hooks/useAvailableShipments';

export const AvailableShipments = () => {
  const { data, isLoading } = useAvailableShipments();
  const { mutate: requestMatch } = useRequestMatch();

  return (
    <PageContainer>
      <DashboardHeader title="Available Shipments" description="Find and bid on new shipments in your area" />
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {data?.map(shipment => (
          <ShipmentCard key={shipment.id} shipment={shipment} onRequestMatch={(id) => requestMatch({ shipmentId: id, bidAmount: 100 })} />
        ))}
      </div>
    </PageContainer>
  );
};
