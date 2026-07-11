import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useTrucks } from '../hooks/useTrucks';
import { TruckCard } from '@/features/fleet/components/TruckCard';
import { Search, Plus, LayoutGrid, List as ListIcon, Filter } from 'lucide-react';

export const TruckDirectory = () => {
  const navigate = useNavigate();
  const [viewMode, setViewMode] = useState('grid');
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  const { data: trucks, isLoading } = useTrucks({ status: statusFilter === 'ALL' ? undefined : statusFilter });

  const filteredTrucks = trucks?.filter(t => 
    t.registrationNumber?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    t.truckType?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  // Mock data if backend is empty
  const mockTrucks = filteredTrucks?.length ? filteredTrucks : [
    { id: 1, registrationNumber: 'MH 12 AB 1234', truckType: 'Flatbed', capacity: 20, status: 'AVAILABLE', lastLocation: 'Mumbai, MH' },
    { id: 2, registrationNumber: 'KA 01 CD 5678', truckType: 'Refrigerated', capacity: 15, status: 'IN_TRANSIT', lastLocation: 'Bangalore, KA' },
    { id: 3, registrationNumber: 'DL 05 EF 9012', truckType: 'Dry Van', capacity: 25, status: 'MAINTENANCE', lastLocation: 'New Delhi, DL' }
  ];

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader title="Truck Directory" description="Manage and track all vehicles in the fleet." />
        <button 
          onClick={() => navigate('/trucks/new')}
          className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors"
        >
          <Plus className="w-4 h-4" /> Add Truck
        </button>
      </div>

      <div className="flex flex-col md:flex-row justify-between items-center gap-4 mb-6 bg-card p-4 rounded-xl border border-border shadow-sm">
        <div className="flex flex-col sm:flex-row gap-4 w-full md:w-auto">
          <div className="relative w-full sm:w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input 
              type="text"
              placeholder="Search trucks..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-9 pr-4 py-2 bg-background border border-input rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <div className="flex gap-2 w-full sm:w-auto overflow-x-auto pb-2 sm:pb-0">
            {['ALL', 'AVAILABLE', 'IN_TRANSIT', 'MAINTENANCE'].map(tab => (
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
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading trucks...</div>
      ) : viewMode === 'grid' ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {mockTrucks.map(truck => (
            <div key={truck.id} onClick={() => navigate(`/trucks/${truck.id}`)} className="cursor-pointer">
              <TruckCard truck={truck} />
            </div>
          ))}
        </div>
      ) : (
        <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
          <table className="w-full text-sm text-left">
            <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
              <tr>
                <th className="px-6 py-4 font-medium">Registration</th>
                <th className="px-6 py-4 font-medium">Type</th>
                <th className="px-6 py-4 font-medium">Capacity</th>
                <th className="px-6 py-4 font-medium">Location</th>
                <th className="px-6 py-4 font-medium">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {mockTrucks.map(truck => (
                <tr key={truck.id} onClick={() => navigate(`/trucks/${truck.id}`)} className="hover:bg-muted/30 transition-colors cursor-pointer">
                  <td className="px-6 py-4 font-medium text-foreground">{truck.registrationNumber}</td>
                  <td className="px-6 py-4">{truck.truckType}</td>
                  <td className="px-6 py-4">{truck.capacity} Tons</td>
                  <td className="px-6 py-4 text-muted-foreground">{truck.lastLocation || 'Unknown'}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 text-xs font-medium rounded-full ${
                      truck.status === 'AVAILABLE' ? 'bg-green-500/10 text-green-600' :
                      truck.status === 'IN_TRANSIT' ? 'bg-blue-500/10 text-blue-600' :
                      'bg-yellow-500/10 text-yellow-600'
                    }`}>
                      {truck.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </PageContainer>
  );
};
