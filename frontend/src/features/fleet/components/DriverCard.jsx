import React from 'react';
import { User, Phone, Map, Star } from 'lucide-react';

export const DriverCard = ({ driver }) => {
  return (
    <div className="p-5 bg-card rounded-xl border border-border shadow-sm flex flex-col">
      <div className="flex items-center gap-4 mb-4">
        <div className="w-12 h-12 rounded-full bg-muted flex items-center justify-center text-muted-foreground">
          <User className="w-6 h-6" />
        </div>
        <div>
          <h4 className="font-semibold text-foreground">{driver.name}</h4>
          <p className="text-xs text-muted-foreground flex items-center gap-1">
            <Star className="w-3 h-3 text-yellow-500 fill-yellow-500" />
            {driver.rating || 'New'} ({driver.completedTrips || 0} trips)
          </p>
        </div>
      </div>
      
      <div className="space-y-2 text-sm text-muted-foreground">
        <div className="flex items-center gap-2">
          <Phone className="w-4 h-4" />
          <span>{driver.contactNumber || 'No contact info'}</span>
        </div>
        <div className="flex items-center gap-2">
          <Map className="w-4 h-4" />
          <span>Status: {driver.status || 'OFFLINE'}</span>
        </div>
      </div>
    </div>
  );
};
