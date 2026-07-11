import React from 'react';
import { CheckCircle, Clock, Truck, Package, MapPin } from 'lucide-react';

const STATUS_ICONS = {
  CREATED: <Package className="w-5 h-5" />,
  MATCHING: <Clock className="w-5 h-5" />,
  ASSIGNED: <Truck className="w-5 h-5" />,
  IN_TRANSIT: <Truck className="w-5 h-5 text-blue-500" />,
  DELIVERED: <MapPin className="w-5 h-5 text-green-500" />,
  COMPLETED: <CheckCircle className="w-5 h-5 text-green-600" />
};

export const ShipmentTimelineLog = ({ events = [] }) => {
  if (!events.length) {
    return <p className="text-sm text-muted-foreground p-4 text-center border rounded-xl">No timeline events available.</p>;
  }

  return (
    <div className="relative border-l-2 border-border ml-4 mt-2 space-y-8 pb-4">
      {events.map((event, index) => {
        const isLast = index === events.length - 1;
        return (
          <div key={event.id} className="relative pl-8">
            <div className={`absolute -left-[17px] top-0 w-8 h-8 rounded-full flex items-center justify-center border-2 border-background ${isLast ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground'}`}>
              {STATUS_ICONS[event.status] || <CheckCircle className="w-4 h-4" />}
            </div>
            
            <div className={`flex flex-col sm:flex-row sm:items-center justify-between gap-2 ${isLast ? 'opacity-100' : 'opacity-70'}`}>
              <div>
                <h4 className={`font-semibold ${isLast ? 'text-foreground' : 'text-muted-foreground'}`}>{event.title}</h4>
                <p className="text-sm mt-1">{event.description}</p>
              </div>
              <div className="text-xs font-medium bg-muted px-2 py-1 rounded-md w-fit h-fit whitespace-nowrap">
                {event.timestamp}
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
};
