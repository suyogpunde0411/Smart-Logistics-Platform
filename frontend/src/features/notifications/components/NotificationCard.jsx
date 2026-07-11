import React from 'react';
import { Truck, Package, Activity, AlertTriangle, ShieldCheck, Info, MapPin } from 'lucide-react';
import { useMarkAsRead, useDeleteNotification } from '../hooks/useNotifications';

const getIconForCategory = (category) => {
  switch (category) {
    case 'TRIP_UPDATE': return <Truck className="w-5 h-5 text-blue-500" />;
    case 'SHIPMENT_UPDATE': return <Package className="w-5 h-5 text-green-500" />;
    case 'MATCHING': return <Activity className="w-5 h-5 text-purple-500" />;
    case 'MAINTENANCE_ALERT': return <AlertTriangle className="w-5 h-5 text-orange-500" />;
    case 'SECURITY': return <ShieldCheck className="w-5 h-5 text-red-500" />;
    case 'SYSTEM': return <Info className="w-5 h-5 text-foreground" />;
    default: return <Info className="w-5 h-5 text-muted-foreground" />;
  }
};

export const NotificationCard = ({ notification, inDrawer = false }) => {
  const { mutate: markAsRead } = useMarkAsRead();
  const { mutate: deleteNotification } = useDeleteNotification();

  const handleReadClick = (e) => {
    e.stopPropagation();
    markAsRead(notification.id);
  };

  const isUnread = !notification.isRead;

  return (
    <div className={`p-4 transition-colors flex gap-4 ${isUnread ? 'bg-primary/5' : 'bg-transparent'} hover:bg-muted/50 ${inDrawer ? 'border-b border-border last:border-b-0' : 'border border-border rounded-xl mb-3 shadow-sm bg-card'}`}>
      
      <div className={`shrink-0 w-10 h-10 rounded-full flex items-center justify-center ${isUnread ? 'bg-background shadow-sm ring-1 ring-border' : 'bg-muted'}`}>
        {getIconForCategory(notification.category)}
      </div>

      <div className="flex-1 min-w-0">
        <div className="flex justify-between items-start gap-2 mb-1">
          <h4 className={`text-sm truncate pr-2 ${isUnread ? 'font-bold text-foreground' : 'font-medium text-muted-foreground'}`}>
            {notification.title}
          </h4>
          <span className="text-xs text-muted-foreground whitespace-nowrap mt-0.5">
            {notification.timestamp}
          </span>
        </div>
        
        <p className={`text-sm line-clamp-2 ${isUnread ? 'text-foreground/90' : 'text-muted-foreground'}`}>
          {notification.message}
        </p>

        {/* Optional Action Button based on payload */}
        {notification.actionUrl && (
          <div className="mt-3">
            <button className="text-xs font-medium text-primary hover:underline flex items-center gap-1">
              View Details &rarr;
            </button>
          </div>
        )}
      </div>

      {!inDrawer && isUnread && (
        <div className="shrink-0 flex items-center justify-center">
          <div className="w-2.5 h-2.5 bg-primary rounded-full"></div>
        </div>
      )}
    </div>
  );
};
