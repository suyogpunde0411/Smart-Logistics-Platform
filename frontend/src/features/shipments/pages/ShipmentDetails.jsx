import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ShipmentTimelineLog } from '../components/ShipmentTimelineLog';
import { ShipmentMatchBoard } from '../components/ShipmentMatchBoard';
import { ShipmentDocumentDropzone } from '../components/ShipmentDocumentDropzone';
import { useShipment } from '../hooks/useShipments';
import { Package, MapPin, Truck, DollarSign, Calendar, Info, Edit3, XCircle } from 'lucide-react';

export const ShipmentDetails = () => {
  const { id } = useParams();
  const [activeTab, setActiveTab] = useState('overview');

  const { data: shipment, isLoading } = useShipment(id);

  // Mock data fallback
  const mockShipment = shipment || {
    id: id || 'SHP-1001',
    name: 'Electronics to Mumbai',
    category: 'ELECTRONICS',
    priority: 'HIGH',
    status: 'MATCHING',
    budget: '45,000',
    weight: '12',
    cargoType: 'PALLETIZED',
    pickup: { address: 'Plot 45, Industrial Area, Phase 1, New Delhi', contact: 'Ramesh Singh, +91 9876543210', date: '2023-11-20' },
    delivery: { address: 'Warehouse C, Andheri East, Mumbai', contact: 'Suresh Kumar, +91 9123456780', date: '2023-11-22' },
    timeline: [
      { id: 1, status: 'CREATED', title: 'Shipment Created', description: 'Basic details and cargo info added.', timestamp: 'Nov 18, 10:00 AM' },
      { id: 2, status: 'MATCHING', title: 'Matching Initiated', description: 'Searching for available trucks in the network.', timestamp: 'Nov 18, 10:30 AM' }
    ],
    bids: [
      { id: 'BID-1', fleetOwnerName: 'Express Logistics', rating: 4.8, truckType: 'Container 20ft', truckRegistration: 'MH 04 AB 1234', driverName: 'Rahul T', amount: 42000, estimatedPickup: 'Nov 20, 08:00 AM' },
      { id: 'BID-2', fleetOwnerName: 'Speed Cargo', rating: 4.5, truckType: 'Container 20ft', truckRegistration: 'DL 01 CD 5678', amount: 46000, estimatedPickup: 'Nov 20, 11:00 AM' }
    ],
    documents: [{ id: 1, type: 'INVOICE' }]
  };

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading shipment details...</div>;

  return (
    <PageContainer>
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
        <div>
          <div className="flex items-center gap-3">
            <h1 className="text-2xl font-bold tracking-tight">{mockShipment.id}</h1>
            <span className="px-2.5 py-1 text-xs font-bold rounded-full bg-yellow-500/10 text-yellow-600 border border-yellow-500/20">{mockShipment.status}</span>
          </div>
          <p className="text-muted-foreground mt-1">{mockShipment.name}</p>
        </div>
        <div className="flex gap-2">
          <button className="flex items-center gap-2 px-4 py-2 bg-secondary text-secondary-foreground rounded-lg font-medium hover:bg-secondary/80 transition-colors">
            <Edit3 className="w-4 h-4" /> Edit
          </button>
          <button className="flex items-center gap-2 px-4 py-2 border border-destructive/30 text-destructive rounded-lg font-medium hover:bg-destructive hover:text-white transition-colors">
            <XCircle className="w-4 h-4" /> Cancel
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <div className="bg-card border border-border rounded-xl p-4 flex items-center gap-4">
          <div className="p-3 bg-primary/10 text-primary rounded-lg"><Package className="w-6 h-6" /></div>
          <div><p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Cargo</p><p className="font-bold">{mockShipment.weight} Tons, {mockShipment.cargoType}</p></div>
        </div>
        <div className="bg-card border border-border rounded-xl p-4 flex items-center gap-4">
          <div className="p-3 bg-primary/10 text-primary rounded-lg"><DollarSign className="w-6 h-6" /></div>
          <div><p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Budget</p><p className="font-bold">₹{mockShipment.budget}</p></div>
        </div>
        <div className="bg-card border border-border rounded-xl p-4 flex items-center gap-4 md:col-span-2">
          <div className="p-3 bg-primary/10 text-primary rounded-lg"><Info className="w-6 h-6" /></div>
          <div>
            <p className="text-xs text-muted-foreground font-medium uppercase tracking-wider">Route</p>
            <div className="flex items-center gap-2 text-sm font-medium">
              <span className="truncate">{mockShipment.pickup.address.split(',').pop()}</span>
              <span className="text-muted-foreground">→</span>
              <span className="truncate">{mockShipment.delivery.address.split(',').pop()}</span>
            </div>
          </div>
        </div>
      </div>

      <div className="flex border-b border-border mb-6 overflow-x-auto">
        {['overview', 'matching & bids', 'timeline', 'documents'].map(tab => (
          <button 
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-3 font-medium text-sm whitespace-nowrap border-b-2 transition-colors ${activeTab === tab ? 'border-primary text-primary' : 'border-transparent text-muted-foreground hover:text-foreground'}`}
          >
            {tab.toUpperCase()}
          </button>
        ))}
      </div>

      <div className="animate-in fade-in slide-in-from-bottom-4">
        {activeTab === 'overview' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-card border border-border rounded-xl p-6 shadow-sm space-y-6">
              <h3 className="font-semibold text-lg flex items-center gap-2"><MapPin className="w-5 h-5 text-primary" /> Pickup Information</h3>
              <div>
                <p className="font-medium">{mockShipment.pickup.address}</p>
                <div className="mt-4 space-y-2 text-sm">
                  <p className="flex items-center gap-2 text-muted-foreground"><Calendar className="w-4 h-4" /> Date: <span className="text-foreground font-medium">{mockShipment.pickup.date}</span></p>
                  <p className="text-muted-foreground">Contact: <span className="text-foreground font-medium">{mockShipment.pickup.contact}</span></p>
                </div>
              </div>
            </div>

            <div className="bg-card border border-border rounded-xl p-6 shadow-sm space-y-6">
              <h3 className="font-semibold text-lg flex items-center gap-2"><MapPin className="w-5 h-5 text-destructive" /> Delivery Information</h3>
              <div>
                <p className="font-medium">{mockShipment.delivery.address}</p>
                <div className="mt-4 space-y-2 text-sm">
                  <p className="flex items-center gap-2 text-muted-foreground"><Calendar className="w-4 h-4" /> Date: <span className="text-foreground font-medium">{mockShipment.delivery.date}</span></p>
                  <p className="text-muted-foreground">Contact: <span className="text-foreground font-medium">{mockShipment.delivery.contact}</span></p>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'matching & bids' && (
          <ShipmentMatchBoard shipmentId={id} bids={mockShipment.bids} />
        )}

        {activeTab === 'timeline' && (
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm max-w-3xl">
            <h3 className="font-semibold text-lg mb-6">Shipment Lifecycle</h3>
            <ShipmentTimelineLog events={mockShipment.timeline} />
          </div>
        )}

        {activeTab === 'documents' && (
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <h3 className="font-semibold text-lg mb-6">Required Documents</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              <ShipmentDocumentDropzone shipmentId={id} documentType="INVOICE" existingDocument={mockShipment.documents.find(d => d.type === 'INVOICE')} />
              <ShipmentDocumentDropzone shipmentId={id} documentType="E_WAY_BILL" existingDocument={mockShipment.documents.find(d => d.type === 'E_WAY_BILL')} />
              <ShipmentDocumentDropzone shipmentId={id} documentType="PURCHASE_ORDER" existingDocument={mockShipment.documents.find(d => d.type === 'PURCHASE_ORDER')} />
            </div>
          </div>
        )}
      </div>

    </PageContainer>
  );
};
