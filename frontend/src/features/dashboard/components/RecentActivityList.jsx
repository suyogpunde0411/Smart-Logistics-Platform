import React from 'react';

export const RecentActivityList = ({ activities }) => (
  <div className="p-6 bg-card rounded-xl border border-border shadow-sm">
    <h3 className="text-lg font-semibold mb-4">Recent Activity</h3>
    <div className="space-y-4">
      {activities?.length > 0 ? (
        activities.map((act, i) => (
          <div key={i} className="flex items-start gap-4">
            <div className="w-2 h-2 mt-2 rounded-full bg-primary flex-shrink-0" />
            <div>
              <p className="text-sm font-medium">{act.title}</p>
              <p className="text-xs text-muted-foreground">{act.time}</p>
            </div>
          </div>
        ))
      ) : (
        <p className="text-sm text-muted-foreground">No recent activity.</p>
      )}
    </div>
  </div>
);
