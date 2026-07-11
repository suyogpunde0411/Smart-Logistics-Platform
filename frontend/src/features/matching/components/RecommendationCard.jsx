import React from 'react';
import { MapPin, Truck, Package, DollarSign, Clock, Star } from 'lucide-react';
import { MatchScoreIndicator } from './MatchScoreIndicator';

export const RecommendationCard = ({ 
  type = 'SHIPMENT', // or 'TRUCK'
  data, 
  onAction 
}) => {
  const isShipment = type === 'SHIPMENT';

  return (
    <div className="bg-card border border-border rounded-xl p-5 hover:border-primary/50 transition-colors shadow-sm flex flex-col h-full">
      <div className="flex justify-between items-start mb-4">
        <div>
          <div className="flex items-center gap-2 mb-1">
            {isShipment ? <Package className="w-4 h-4 text-primary" /> : <Truck className="w-4 h-4 text-primary" />}
            <h4 className="font-semibold text-sm line-clamp-1">{isShipment ? data.name : data.truckRegistration}</h4>
          </div>
          <p className="text-xs text-muted-foreground">{isShipment ? `${data.weight} Tons • ${data.cargoType}` : `${data.capacity} Tons • ${data.truckType}`}</p>
        </div>
        <MatchScoreIndicator score={data.matchScore} size="sm" />
      </div>
      
      <div className="space-y-3 mb-6 flex-1">
        {isShipment ? (
          <>
            <div className="flex items-start gap-3">
              <div className="mt-0.5"><MapPin className="w-4 h-4 text-muted-foreground" /></div>
              <div>
                <p className="text-xs text-muted-foreground">Pickup</p>
                <p className="text-sm font-medium line-clamp-1">{data.origin}</p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <div className="mt-0.5"><MapPin className="w-4 h-4 text-destructive" /></div>
              <div>
                <p className="text-xs text-muted-foreground">Dropoff</p>
                <p className="text-sm font-medium line-clamp-1">{data.destination}</p>
              </div>
            </div>
          </>
        ) : (
          <>
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-muted flex items-center justify-center shrink-0">
                <Star className="w-4 h-4 text-yellow-500 fill-current" />
              </div>
              <div>
                <p className="text-xs text-muted-foreground">Driver Rating</p>
                <p className="text-sm font-medium">{data.driverRating} ({data.totalTrips} Trips)</p>
              </div>
            </div>
            <div className="flex items-start gap-3">
              <div className="mt-0.5"><MapPin className="w-4 h-4 text-muted-foreground" /></div>
              <div>
                <p className="text-xs text-muted-foreground">Current Location</p>
                <p className="text-sm font-medium">{data.currentLocation} ({data.distanceAway}km away)</p>
              </div>
            </div>
          </>
        )}
      </div>

      <div className="pt-4 border-t border-border flex justify-between items-center text-sm mb-4">
        <div className="flex items-center gap-1.5 text-muted-foreground">
          <Clock className="w-4 h-4" /> {isShipment ? data.eta : 'Available Now'}
        </div>
        <div className="font-semibold text-foreground flex items-center gap-1">
          <DollarSign className="w-4 h-4 text-green-600" /> {data.estimatedCost || data.budget}
        </div>
      </div>

      <button 
        onClick={() => onAction(data)}
        className="w-full py-2 bg-primary/10 text-primary font-medium rounded-lg hover:bg-primary hover:text-primary-foreground transition-colors"
      >
        {isShipment ? 'Place Bid' : 'View Truck'}
      </button>
    </div>
  );
};
