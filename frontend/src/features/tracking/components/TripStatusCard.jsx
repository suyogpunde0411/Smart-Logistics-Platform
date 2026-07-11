import React from 'react';
import { Truck, User, MapPin, PhoneCall } from 'lucide-react';

export const TripStatusCard = ({ tripData }) => {
  if (!tripData) return null;

  return (
    <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
      <div className="p-4 border-b border-border bg-muted/30 flex justify-between items-center">
        <div>
          <p className="text-xs font-medium text-muted-foreground uppercase tracking-wider">Trip ID</p>
          <p className="font-bold">{tripData.id}</p>
        </div>
        <div className="px-3 py-1 bg-blue-500/10 text-blue-600 font-bold text-sm rounded-full border border-blue-500/20">
          {tripData.status?.replace('_', ' ')}
        </div>
      </div>
      
      <div className="p-6 space-y-6">
        {/* Route Info */}
        <div>
          <div className="flex items-start gap-3 mb-4">
            <div className="mt-0.5"><MapPin className="w-5 h-5 text-muted-foreground" /></div>
            <div>
              <p className="text-xs text-muted-foreground font-medium">Origin</p>
              <p className="text-sm font-medium">{tripData.origin}</p>
            </div>
          </div>
          <div className="flex items-start gap-3">
            <div className="mt-0.5"><MapPin className="w-5 h-5 text-destructive" /></div>
            <div>
              <p className="text-xs text-muted-foreground font-medium">Destination</p>
              <p className="text-sm font-medium">{tripData.destination}</p>
            </div>
          </div>
        </div>

        <hr className="border-border" />

        {/* Truck & Driver Info */}
        <div className="grid grid-cols-2 gap-4">
          <div className="flex items-start gap-3">
            <div className="p-2 bg-primary/10 rounded-lg shrink-0"><Truck className="w-4 h-4 text-primary" /></div>
            <div>
              <p className="text-xs text-muted-foreground font-medium">Vehicle</p>
              <p className="text-sm font-medium">{tripData.truckRegistration}</p>
              <p className="text-xs text-muted-foreground mt-0.5">{tripData.truckType}</p>
            </div>
          </div>

          <div className="flex items-start gap-3">
            <div className="p-2 bg-primary/10 rounded-lg shrink-0"><User className="w-4 h-4 text-primary" /></div>
            <div>
              <p className="text-xs text-muted-foreground font-medium">Driver</p>
              <p className="text-sm font-medium">{tripData.driverName}</p>
              <button className="text-xs text-primary flex items-center gap-1 mt-1 hover:underline">
                <PhoneCall className="w-3 h-3" /> Contact
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
