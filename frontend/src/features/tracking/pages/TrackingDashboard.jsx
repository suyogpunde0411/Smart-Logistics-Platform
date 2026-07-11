import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useActiveTrips } from '../hooks/useTrackingCore';
import { Search, Filter, MapPin, Truck, Navigation2 } from 'lucide-react';

export const TrackingDashboard = () => {
  const navigate = useNavigate();
  const { currentRole } = useSelector((state) => state.auth);
  const [searchQuery, setSearchQuery] = useState('');
  
  const { data: trips, isLoading } = useActiveTrips();

  // Mock data fallback
  const activeTrips = trips || [
    { id: 'TRP-101', shipment: 'Electronics', truck: 'MH 12 AB 1234', origin: 'Mumbai, MH', destination: 'Delhi, DL', status: 'IN_TRANSIT', eta: 'Tomorrow, 14:00', progress: 65 },
    { id: 'TRP-102', shipment: 'Textiles', truck: 'GJ 05 CD 5678', origin: 'Surat, GJ', destination: 'Bangalore, KA', status: 'LOADING', eta: 'Nov 24, 09:00', progress: 5 },
    { id: 'TRP-103', shipment: 'Auto Parts', truck: 'TN 01 EF 9012', origin: 'Chennai, TN', destination: 'Pune, MH', status: 'IN_TRANSIT', eta: 'Today, 22:30', progress: 85 },
  ];

  const getStatusColor = (status) => {
    switch (status) {
      case 'IN_TRANSIT': return 'bg-blue-500/10 text-blue-600 border-blue-500/20';
      case 'LOADING': return 'bg-yellow-500/10 text-yellow-600 border-yellow-500/20';
      case 'COMPLETED': return 'bg-green-500/10 text-green-600 border-green-500/20';
      default: return 'bg-muted text-muted-foreground border-border';
    }
  };

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader 
          title="Active Trips Tracking" 
          description={currentRole === 'Admin' ? 'Global fleet tracking and logistics oversight.' : 'Track the live progress of your active shipments and trucks.'} 
        />
      </div>

      <div className="flex flex-col md:flex-row gap-4 mb-6 bg-card p-4 rounded-xl border border-border shadow-sm">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <input 
            type="text"
            placeholder="Search by Trip ID, Truck, or Location..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 bg-background border border-input rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
        <button className="flex items-center gap-2 px-4 py-2 border border-border rounded-lg text-sm font-medium bg-background hover:bg-muted transition-colors whitespace-nowrap">
          <Filter className="w-4 h-4" /> Filters
        </button>
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading active trips...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {activeTrips.map(trip => (
            <div 
              key={trip.id} 
              onClick={() => navigate(`/tracking/${trip.id}`)}
              className="bg-card border border-border rounded-xl p-5 hover:border-primary/50 cursor-pointer transition-colors shadow-sm flex flex-col h-full"
            >
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h4 className="font-bold text-lg">{trip.id}</h4>
                  <p className="text-xs text-muted-foreground">{trip.shipment}</p>
                </div>
                <span className={`px-2.5 py-1 text-xs font-bold rounded-full border ${getStatusColor(trip.status)}`}>
                  {trip.status.replace('_', ' ')}
                </span>
              </div>
              
              <div className="space-y-3 mb-6 flex-1">
                <div className="flex items-center gap-2 text-sm">
                  <div className="p-1.5 bg-muted rounded-md"><Truck className="w-3.5 h-3.5 text-muted-foreground" /></div>
                  <span className="font-medium">{trip.truck}</span>
                </div>
                <div className="flex items-start gap-2">
                  <div className="mt-1"><MapPin className="w-4 h-4 text-primary" /></div>
                  <div>
                    <p className="text-sm">{trip.origin}</p>
                    <div className="h-4 border-l-2 border-dashed border-border ml-1.5 my-1"></div>
                    <p className="text-sm">{trip.destination}</p>
                  </div>
                </div>
              </div>

              <div className="pt-4 border-t border-border">
                <div className="flex justify-between items-center text-xs mb-2">
                  <span className="text-muted-foreground flex items-center gap-1"><Navigation2 className="w-3 h-3" /> ETA</span>
                  <span className="font-medium text-foreground">{trip.eta}</span>
                </div>
                <div className="w-full h-1.5 bg-muted rounded-full overflow-hidden">
                  <div className="h-full bg-primary rounded-full" style={{ width: `${trip.progress}%` }}></div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </PageContainer>
  );
};
