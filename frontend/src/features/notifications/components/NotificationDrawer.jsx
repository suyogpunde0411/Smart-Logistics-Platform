import React, { useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Settings, CheckSquare, X } from 'lucide-react';
import { NotificationCard } from './NotificationCard';
import { useNotifications, useMarkAllAsRead } from '../hooks/useNotifications';

export const NotificationDrawer = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const drawerRef = useRef(null);
  
  const { data: notifications, isLoading } = useNotifications({ limit: 5 });
  const { mutate: markAllAsRead, isPending } = useMarkAllAsRead();

  // Close drawer on click outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (drawerRef.current && !drawerRef.current.contains(event.target)) {
        onClose();
      }
    };
    if (isOpen) document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  // Mock data fallback
  const mockNotifications = notifications || [
    { id: 1, category: 'TRIP_UPDATE', title: 'Trip Started', message: 'Truck MH 12 AB 1234 has departed for Delhi.', timestamp: '2 mins ago', isRead: false, actionUrl: '/tracking/TRP-101' },
    { id: 2, category: 'MATCHING', title: 'New Bid Received', message: 'Express Logistics placed a bid of ₹42,000.', timestamp: '1 hour ago', isRead: false },
    { id: 3, category: 'SYSTEM', title: 'Scheduled Maintenance', message: 'Platform will be offline for 2 hours tonight.', timestamp: 'Yesterday', isRead: true }
  ];

  const handleViewAll = () => {
    onClose();
    navigate('/notifications');
  };

  return (
    <div className="absolute right-0 top-12 w-80 sm:w-96 bg-card border border-border shadow-xl rounded-xl z-50 overflow-hidden animate-in fade-in slide-in-from-top-2" ref={drawerRef}>
      
      {/* Drawer Header */}
      <div className="flex justify-between items-center p-4 border-b border-border bg-muted/20">
        <h3 className="font-semibold">Notifications</h3>
        <div className="flex items-center gap-1">
          <button 
            onClick={() => markAllAsRead()}
            disabled={isPending}
            className="p-1.5 text-muted-foreground hover:text-foreground hover:bg-muted rounded-md transition-colors"
            title="Mark all as read"
          >
            <CheckSquare className="w-4 h-4" />
          </button>
          <button 
            onClick={() => { onClose(); navigate('/settings/notifications'); }}
            className="p-1.5 text-muted-foreground hover:text-foreground hover:bg-muted rounded-md transition-colors"
            title="Notification Settings"
          >
            <Settings className="w-4 h-4" />
          </button>
          <button 
            onClick={onClose}
            className="p-1.5 text-muted-foreground hover:text-foreground hover:bg-muted rounded-md transition-colors sm:hidden"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Drawer Content */}
      <div className="max-h-[400px] overflow-y-auto overscroll-contain">
        {isLoading ? (
          <div className="p-8 text-center text-muted-foreground text-sm">Loading...</div>
        ) : mockNotifications.length === 0 ? (
          <div className="p-8 text-center text-muted-foreground text-sm flex flex-col items-center">
            <div className="w-12 h-12 bg-muted rounded-full flex items-center justify-center mb-3">
              <CheckSquare className="w-5 h-5 opacity-50" />
            </div>
            <p>You're all caught up!</p>
          </div>
        ) : (
          <div className="flex flex-col">
            {mockNotifications.map(notif => (
              <NotificationCard key={notif.id} notification={notif} inDrawer={true} />
            ))}
          </div>
        )}
      </div>

      {/* Drawer Footer */}
      <div className="p-3 border-t border-border bg-muted/10 text-center">
        <button 
          onClick={handleViewAll}
          className="text-sm font-medium text-primary hover:underline"
        >
          View All Notifications
        </button>
      </div>

    </div>
  );
};
