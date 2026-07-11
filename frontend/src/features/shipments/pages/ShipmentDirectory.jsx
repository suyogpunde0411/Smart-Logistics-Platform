import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useShipments } from '../hooks/useShipments';
import { Search, Plus, LayoutGrid, List as ListIcon, MapPin, Calendar, Package } from 'lucide-react';

export const ShipmentDirectory = () => {
  const navigate = useNavigate();
  const [viewMode, setViewMode] = useState('list');
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  const { data: shipments, isLoading } = useShipments({ status: statusFilter === 'ALL' ? undefined : statusFilter });

  // Mock data fallback
  const mockShipments = shipments || [
    { id: 'SHP-1001', name: 'Electronics to Mumbai', origin: 'Delhi, DL', destination: 'Mumbai, MH', date: '2023-11-20', status: 'AVAILABLE', budget: '₹45,000' },
    { id: 'SHP-1002', name: 'Textiles Batch A', origin: 'Surat, GJ', destination: 'Bangalore, KA', date: '2023-11-21', status: 'IN_TRANSIT', budget: '₹32,000' },
    { id: 'SHP-1003', name: 'Auto Parts Fast', origin: 'Chennai, TN', destination: 'Pune, MH', date: '2023-11-19', status: 'MATCHING', budget: '₹28,500' }
  ];

  const getStatusColor = (status) => {
    switch (status) {
      case 'AVAILABLE': return 'bg-green-500/10 text-green-600';
      case 'MATCHING': return 'bg-yellow-500/10 text-yellow-600';
      case 'IN_TRANSIT': return 'bg-blue-500/10 text-blue-600';
      case 'DELIVERED': return 'bg-emerald-500/10 text-emerald-600';
      default: return 'bg-muted text-muted-foreground';
    }
  };

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader title="Shipment Directory" description="Manage and track all shipments across the network." />
        <button 
          onClick={() => navigate('/shipments/new')}
          className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors"
        >
          <Plus className="w-4 h-4" /> Create Shipment
        </button>
      </div>

      <div className="flex flex-col md:flex-row justify-between items-center gap-4 mb-6 bg-card p-4 rounded-xl border border-border shadow-sm">
        <div className="flex flex-col sm:flex-row gap-4 w-full md:w-auto">
          <div className="relative w-full sm:w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input 
              type="text"
              placeholder="Search by ID or city..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-9 pr-4 py-2 bg-background border border-input rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <div className="flex gap-2 w-full sm:w-auto overflow-x-auto pb-2 sm:pb-0">
            {['ALL', 'AVAILABLE', 'MATCHING', 'IN_TRANSIT', 'DELIVERED'].map(tab => (
              <button 
                key={tab}
                onClick={() => setStatusFilter(tab)}
                className={`px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-colors ${statusFilter === tab ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground hover:bg-muted/80'}`}
              >
                {tab.replace('_', ' ')}
              </button>
            ))}
          </div>
        </div>

        <div className="flex items-center gap-2 border border-border rounded-lg p-1 bg-background">
          <button 
            onClick={() => setViewMode('grid')}
            className={`p-2 rounded-md transition-colors ${viewMode === 'grid' ? 'bg-muted text-foreground shadow-sm' : 'text-muted-foreground hover:bg-muted/50'}`}
          >
            <LayoutGrid className="w-4 h-4" />
          </button>
          <button 
            onClick={() => setViewMode('list')}
            className={`p-2 rounded-md transition-colors ${viewMode === 'list' ? 'bg-muted text-foreground shadow-sm' : 'text-muted-foreground hover:bg-muted/50'}`}
          >
            <ListIcon className="w-4 h-4" />
          </button>
        </div>
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading shipments...</div>
      ) : viewMode === 'list' ? (
        <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
          <table className="w-full text-sm text-left">
            <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
              <tr>
                <th className="px-6 py-4 font-medium">ID & Name</th>
                <th className="px-6 py-4 font-medium">Route</th>
                <th className="px-6 py-4 font-medium">Pickup Date</th>
                <th className="px-6 py-4 font-medium">Budget</th>
                <th className="px-6 py-4 font-medium">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {mockShipments.map(shipment => (
                <tr key={shipment.id} onClick={() => navigate(`/shipments/${shipment.id}`)} className="hover:bg-muted/30 transition-colors cursor-pointer">
                  <td className="px-6 py-4">
                    <p className="font-semibold text-foreground">{shipment.id}</p>
                    <p className="text-muted-foreground text-xs">{shipment.name}</p>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <span className="truncate max-w-[100px]">{shipment.origin}</span>
                      <span className="text-muted-foreground">→</span>
                      <span className="truncate max-w-[100px]">{shipment.destination}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-muted-foreground">{shipment.date}</td>
                  <td className="px-6 py-4 font-medium">{shipment.budget}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 text-xs font-bold rounded-full ${getStatusColor(shipment.status)}`}>
                      {shipment.status.replace('_', ' ')}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
          {mockShipments.map(shipment => (
            <div key={shipment.id} onClick={() => navigate(`/shipments/${shipment.id}`)} className="bg-card border border-border rounded-xl p-5 hover:border-primary/50 cursor-pointer transition-colors shadow-sm flex flex-col h-full">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h4 className="font-semibold">{shipment.id}</h4>
                  <p className="text-xs text-muted-foreground line-clamp-1">{shipment.name}</p>
                </div>
                <span className={`px-2.5 py-1 text-xs font-bold rounded-full ${getStatusColor(shipment.status)}`}>
                  {shipment.status.replace('_', ' ')}
                </span>
              </div>
              
              <div className="space-y-3 mb-6 flex-1">
                <div className="flex items-start gap-3">
                  <div className="mt-0.5"><MapPin className="w-4 h-4 text-primary" /></div>
                  <div>
                    <p className="text-xs text-muted-foreground">Origin</p>
                    <p className="text-sm font-medium">{shipment.origin}</p>
                  </div>
                </div>
                <div className="flex items-start gap-3">
                  <div className="mt-0.5"><MapPin className="w-4 h-4 text-destructive" /></div>
                  <div>
                    <p className="text-xs text-muted-foreground">Destination</p>
                    <p className="text-sm font-medium">{shipment.destination}</p>
                  </div>
                </div>
              </div>

              <div className="pt-4 border-t border-border flex justify-between items-center text-sm">
                <div className="flex items-center gap-1.5 text-muted-foreground">
                  <Calendar className="w-4 h-4" /> {shipment.date}
                </div>
                <div className="font-semibold text-foreground">
                  {shipment.budget}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </PageContainer>
  );
};
