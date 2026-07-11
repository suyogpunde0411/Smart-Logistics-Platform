import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { TripMap } from '../components/TripMap';
import { TripTimeline } from '../components/TripTimeline';
import { ETAWidget } from '../components/ETAWidget';
import { TripStatusCard } from '../components/TripStatusCard';
import { useTripDetails, useTripTimeline } from '../hooks/useTrackingCore';
import { useLiveLocation, useETA } from '../hooks/useLiveTracking';
import { ArrowLeft, RefreshCw, AlertCircle } from 'lucide-react';

export const LiveTripDetails = () => {
  const { tripId } = useParams();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('map');

  // Fetch data using TanStack Query hooks (which include polling where appropriate)
  const { data: tripData, isLoading: tripLoading } = useTripDetails(tripId);
  const { data: timelineData } = useTripTimeline(tripId);
  const { data: liveLocation, isFetching: locationUpdating } = useLiveLocation(tripId);
  const { data: etaData } = useETA(tripId);

  // Mock Data Fallbacks if backend isn't returning data yet
  const mockTrip = tripData || {
    id: tripId || 'TRP-101',
    status: 'IN_TRANSIT',
    origin: 'Mumbai Central, MH',
    destination: 'New Delhi, DL',
    truckRegistration: 'MH 12 AB 1234',
    truckType: 'Container 20ft',
    driverName: 'Ramesh Singh'
  };

  const mockTimeline = timelineData || [
    { id: 1, status: 'CREATED', title: 'Trip Created', description: 'System generated trip TRP-101.', timestamp: 'Nov 20, 08:00 AM' },
    { id: 2, status: 'ASSIGNED', title: 'Driver Assigned', description: 'Ramesh Singh assigned to truck MH 12 AB 1234.', timestamp: 'Nov 20, 08:30 AM' },
    { id: 3, status: 'STARTED', title: 'Trip Started', description: 'Truck departed from depot.', timestamp: 'Nov 20, 09:00 AM' },
    { id: 4, status: 'PICKUP_REACHED', title: 'Reached Origin', description: 'Arrived at Mumbai Central warehouse.', timestamp: 'Nov 20, 10:00 AM', location: 'Mumbai Central' },
    { id: 5, status: 'LOADING_COMPLETED', title: 'Loading Completed', description: '12 Tons of Electronics loaded securely.', timestamp: 'Nov 20, 12:30 PM', location: 'Mumbai Central' },
    { id: 6, status: 'IN_TRANSIT', title: 'In Transit', description: 'Departed towards New Delhi.', timestamp: 'Nov 20, 01:00 PM', location: 'Mumbai Outskirts' }
  ];

  const mockEta = etaData || {
    estimatedTime: '14:30',
    estimatedDate: 'Tomorrow, Nov 22',
    remainingDistance: 640,
    totalDistance: 1420,
    coveredDistance: 780
  };

  // Mock Coordinates: Mumbai to Delhi route
  const originLatLng = [18.9690, 72.8205]; // Mumbai
  const destLatLng = [28.6139, 77.2090]; // Delhi
  const mockLiveLocation = liveLocation?.coordinates || [23.0225, 72.5714]; // Somewhere near Ahmedabad
  const mockRoute = [
    originLatLng,
    [20.3045, 72.9691], // Vapi
    [21.1702, 72.8311], // Surat
    mockLiveLocation,   // Current
    [24.5854, 73.7125], // Udaipur
    [26.9124, 75.7873], // Jaipur
    destLatLng
  ];

  if (tripLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading Live Trip Details...</div>;

  return (
    <PageContainer>
      <div className="mb-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <button 
            onClick={() => navigate('/tracking')}
            className="flex items-center gap-2 text-sm font-medium text-muted-foreground hover:text-foreground mb-4 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" /> Back to Tracking Dashboard
          </button>
          <div className="flex items-center gap-3">
            <h1 className="text-2xl font-bold tracking-tight">Live Trip: {mockTrip.id}</h1>
            {locationUpdating && <RefreshCw className="w-4 h-4 text-primary animate-spin" title="Fetching live GPS..." />}
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        
        {/* Left Column: Map & Tabs */}
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden flex flex-col" style={{ height: '600px' }}>
            <div className="flex border-b border-border">
              {['map', 'timeline'].map(tab => (
                <button 
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`flex-1 py-3 font-medium text-sm border-b-2 transition-colors ${activeTab === tab ? 'border-primary text-primary bg-primary/5' : 'border-transparent text-muted-foreground hover:text-foreground hover:bg-muted/50'}`}
                >
                  {tab === 'map' ? 'Live Map View' : 'Event Timeline'}
                </button>
              ))}
            </div>
            
            <div className="flex-1 relative bg-muted/20 overflow-y-auto">
              {activeTab === 'map' ? (
                <TripMap 
                  origin={originLatLng} 
                  destination={destLatLng} 
                  currentLocation={mockLiveLocation}
                  routeCoordinates={mockRoute}
                />
              ) : (
                <div className="p-6">
                  <TripTimeline events={mockTimeline} />
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Right Column: Widgets */}
        <div className="space-y-6">
          <ETAWidget etaData={mockEta} isLive={mockTrip.status === 'IN_TRANSIT'} />
          <TripStatusCard tripData={mockTrip} />
          
          <div className="bg-yellow-500/10 border border-yellow-500/20 rounded-xl p-4 flex gap-3 text-yellow-700">
            <AlertCircle className="w-5 h-5 shrink-0 mt-0.5" />
            <div className="text-sm">
              <p className="font-semibold mb-1">Simulated Live Feed</p>
              <p>GPS coordinates and ETA are updating automatically in the background via TanStack Query polling (every 10s).</p>
            </div>
          </div>
        </div>

      </div>
    </PageContainer>
  );
};
