import React from 'react';
import { Bell } from 'lucide-react';

export const NotificationWidget = ({ notifications }) => (
  <div className="p-6 bg-card rounded-xl border border-border shadow-sm">
    <div className="flex items-center gap-2 mb-4">
      <Bell className="w-5 h-5 text-muted-foreground" />
      <h3 className="text-lg font-semibold">Notifications</h3>
    </div>
    <div className="space-y-3">
      {notifications?.length > 0 ? (
        notifications.map((notif, i) => (
          <div key={i} className="p-3 bg-muted/50 rounded-md border border-border/50">
            <p className="text-sm font-medium">{notif.title}</p>
            <p className="text-xs text-muted-foreground mt-0.5">{notif.time}</p>
          </div>
        ))
      ) : (
        <p className="text-sm text-muted-foreground">You're all caught up.</p>
      )}
    </div>
  </div>
);
