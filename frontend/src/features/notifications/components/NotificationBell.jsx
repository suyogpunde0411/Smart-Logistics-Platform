import React, { useState } from 'react';
import { Bell } from 'lucide-react';
import { NotificationDrawer } from './NotificationDrawer';
import { useUnreadCount } from '../hooks/useNotifications';

export const NotificationBell = () => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  
  // Polling hook to get the count of unread notifications
  const { data: unreadData } = useUnreadCount();
  const unreadCount = unreadData?.count || 2; // Default mock 2

  return (
    <div className="relative">
      <button 
        onClick={() => setIsDrawerOpen(!isDrawerOpen)}
        className={`relative p-2 rounded-full transition-colors ${isDrawerOpen ? 'bg-primary/10 text-primary' : 'text-muted-foreground hover:bg-muted hover:text-foreground'}`}
        aria-label="Notifications"
      >
        <Bell className="w-5 h-5" />
        
        {/* Unread Badge */}
        {unreadCount > 0 && (
          <span className="absolute top-1.5 right-1.5 flex h-2.5 w-2.5">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-destructive opacity-75"></span>
            <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-destructive border border-background"></span>
          </span>
        )}
      </button>

      <NotificationDrawer 
        isOpen={isDrawerOpen} 
        onClose={() => setIsDrawerOpen(false)} 
      />
    </div>
  );
};
