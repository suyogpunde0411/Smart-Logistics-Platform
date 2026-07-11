import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { RecommendationCard } from '../components/RecommendationCard';
import { BidSubmissionModal } from '../components/BidSubmissionModal';
import { Search, Filter, RefreshCw } from 'lucide-react';

export const RecommendationDirectory = () => {
  const { currentRole } = useSelector((state) => state.auth);
  
  const isSupply = currentRole === 'Driver' || currentRole === 'Fleet Owner';
  const type = isSupply ? 'SHIPMENT' : 'TRUCK';

  const [isRefreshing, setIsRefreshing] = useState(false);
  const [selectedShipment, setSelectedShipment] = useState(null);

  const handleRefresh = () => {
    setIsRefreshing(true);
    setTimeout(() => setIsRefreshing(false), 1000);
  };

  // Mock Data
  const recommendations = isSupply ? [
    { id: 1, name: 'Textiles to Mumbai', weight: 15, cargoType: 'PALLETIZED', origin: 'Surat, GJ', destination: 'Mumbai, MH', matchScore: 95, eta: '4 Hours', budget: '₹12,000' },
    { id: 2, name: 'Auto Parts to Pune', weight: 8, cargoType: 'LOOSE', origin: 'Chennai, TN', destination: 'Pune, MH', matchScore: 82, eta: '18 Hours', budget: '₹35,000' },
    { id: 3, name: 'Frozen Food to Delhi', weight: 12, cargoType: 'TEMPERATURE_CONTROLLED', origin: 'Amritsar, PB', destination: 'New Delhi, DL', matchScore: 45, eta: '6 Hours', budget: '₹18,000' }
  ] : [
    { id: 1, truckRegistration: 'MH 12 AB 1234', capacity: 20, truckType: 'FLATBED', driverRating: 4.8, totalTrips: 142, currentLocation: 'Navi Mumbai', distanceAway: 12, matchScore: 98, estimatedCost: '₹14,500' },
    { id: 2, truckRegistration: 'GJ 05 CD 5678', capacity: 15, truckType: 'DRY_VAN', driverRating: 4.2, totalTrips: 89, currentLocation: 'Surat', distanceAway: 45, matchScore: 76, estimatedCost: '₹11,000' }
  ];

  const handleAction = (data) => {
    if (isSupply) {
      setSelectedShipment(data);
    } else {
      // In a real app, clicking 'View Truck' would take you to the Truck Details or direct message
      alert(`Viewing Truck: ${data.truckRegistration}`);
    }
  };

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader 
          title="AI Recommendations" 
          description={isSupply ? 'Shipments that perfectly match your truck profile and location.' : 'Trucks currently near your pickup location with high compatibility.'} 
        />
        <button 
          onClick={handleRefresh}
          disabled={isRefreshing}
          className="flex items-center gap-2 bg-secondary text-secondary-foreground px-4 py-2 rounded-lg font-medium hover:bg-secondary/80 transition-colors disabled:opacity-50"
        >
          <RefreshCw className={`w-4 h-4 ${isRefreshing ? 'animate-spin' : ''}`} /> Refresh Matches
        </button>
      </div>

      <div className="flex flex-col md:flex-row gap-4 mb-6 bg-card p-4 rounded-xl border border-border shadow-sm">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <input 
            type="text"
            placeholder={`Search ${isSupply ? 'cities or cargo type' : 'truck type or location'}...`}
            className="w-full pl-9 pr-4 py-2 bg-background border border-input rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
        <button className="flex items-center gap-2 px-4 py-2 border border-border rounded-lg text-sm font-medium bg-background hover:bg-muted transition-colors whitespace-nowrap">
          <Filter className="w-4 h-4" /> Filters
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {recommendations.map(rec => (
          <RecommendationCard key={rec.id} type={type} data={rec} onAction={handleAction} />
        ))}
      </div>

      {isSupply && (
        <BidSubmissionModal 
          isOpen={!!selectedShipment} 
          shipment={selectedShipment} 
          onClose={() => setSelectedShipment(null)} 
        />
      )}
    </PageContainer>
  );
};
