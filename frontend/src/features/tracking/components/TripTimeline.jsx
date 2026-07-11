import React from 'react';
import { CheckCircle, Clock, Truck, Package, MapPin, Coffee } from 'lucide-react';

const STATUS_ICONS = {
  CREATED: <Package className="w-5 h-5" />,
  ASSIGNED: <CheckCircle className="w-5 h-5" />,
  STARTED: <Truck className="w-5 h-5 text-blue-500" />,
  PICKUP_REACHED: <MapPin className="w-5 h-5 text-yellow-600" />,
  LOADING_COMPLETED: <Package className="w-5 h-5 text-green-500" />,
  IN_TRANSIT: <Truck className="w-5 h-5 text-blue-500" />,
  REST_STOP: <Coffee className="w-5 h-5 text-orange-500" />,
  DESTINATION_REACHED: <MapPin className="w-5 h-5 text-green-500" />,
  COMPLETED: <CheckCircle className="w-5 h-5 text-green-600" />
};

export const TripTimeline = ({ events = [] }) => {
  if (!events.length) {
    return <div className="p-4 text-center text-muted-foreground border rounded-xl bg-card">No timeline events available yet.</div>;
  }

  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <h3 className="font-semibold text-lg mb-6">Trip Timeline</h3>
      
      <div className="relative border-l-2 border-border ml-4 space-y-8 pb-4">
        {events.map((event, index) => {
          const isLatest = index === events.length - 1;
          const isPast = index < events.length - 1;
          
          return (
            <div key={event.id} className="relative pl-8">
              <div className={`absolute -left-[17px] top-0 w-8 h-8 rounded-full flex items-center justify-center border-2 border-background z-10 ${
                isLatest ? 'bg-primary text-primary-foreground shadow-md ring-4 ring-primary/20' : 
                isPast ? 'bg-muted text-muted-foreground' : 'bg-muted/50 text-muted-foreground/50'
              }`}>
                {STATUS_ICONS[event.status] || <Clock className="w-4 h-4" />}
              </div>
              
              <div className={`flex flex-col sm:flex-row sm:items-start justify-between gap-2 ${!isLatest && !isPast ? 'opacity-50' : ''}`}>
                <div>
                  <h4 className={`font-semibold ${isLatest ? 'text-primary' : 'text-foreground'}`}>{event.title}</h4>
                  <p className="text-sm text-muted-foreground mt-1">{event.description}</p>
                  {event.location && (
                    <p className="text-xs text-muted-foreground mt-2 flex items-center gap-1">
                      <MapPin className="w-3 h-3" /> {event.location}
                    </p>
                  )}
                </div>
                {event.timestamp && (
                  <div className="text-xs font-medium bg-muted px-2 py-1 rounded-md w-fit h-fit whitespace-nowrap mt-1 sm:mt-0">
                    {event.timestamp}
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
