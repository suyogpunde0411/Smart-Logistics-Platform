import React from 'react';
import { Map, MapPin, Navigation, Clock } from 'lucide-react';

export const TripCard = ({ trip, onUpdateStatus }) => {
  return (
    <div className="p-5 bg-card rounded-xl border border-border shadow-sm">
      <div className="flex justify-between items-start mb-4">
        <div className="flex items-center gap-2">
          <div className="p-2 bg-blue-500/10 rounded-md">
            <Map className="w-5 h-5 text-blue-500" />
          </div>
          <div>
            <h3 className="font-semibold text-foreground">Trip #{trip.id}</h3>
            <p className="text-xs text-muted-foreground">Status: {trip.status}</p>
          </div>
        </div>
      </div>

      <div className="space-y-3 mb-5">
        <div className="flex items-center gap-3 text-sm">
          <MapPin className="w-4 h-4 text-muted-foreground" />
          <div>
            <p className="text-muted-foreground text-xs">Origin</p>
            <p className="font-medium">{trip.origin}</p>
          </div>
        </div>
        <div className="flex items-center gap-3 text-sm">
          <Navigation className="w-4 h-4 text-muted-foreground" />
          <div>
            <p className="text-muted-foreground text-xs">Destination</p>
            <p className="font-medium">{trip.destination}</p>
          </div>
        </div>
        <div className="flex items-center gap-3 text-sm">
          <Clock className="w-4 h-4 text-muted-foreground" />
          <div>
            <p className="text-muted-foreground text-xs">ETA</p>
            <p className="font-medium">{trip.eta || 'Calculating...'}</p>
          </div>
        </div>
      </div>

      {trip.status !== 'COMPLETED' && (
        <button 
          onClick={() => onUpdateStatus(trip.id, 'IN_TRANSIT')}
          className="w-full py-2 bg-secondary text-secondary-foreground rounded-md text-sm font-medium hover:bg-secondary/80 transition-colors"
        >
          Update Status
        </button>
      )}
    </div>
  );
};
