import React from 'react';
import { Wrench, AlertTriangle, Calendar } from 'lucide-react';

export const MaintenanceCard = ({ log }) => {
  const isOverdue = new Date(log.dueDate) < new Date();
  
  return (
    <div className={`p-4 rounded-xl border shadow-sm ${isOverdue ? 'bg-destructive/5 border-destructive/20' : 'bg-card border-border'}`}>
      <div className="flex justify-between items-start mb-3">
        <div className="flex items-center gap-2">
          <div className={`w-8 h-8 rounded-full flex items-center justify-center ${isOverdue ? 'bg-destructive/10' : 'bg-primary/10'}`}>
            <Wrench className={`w-4 h-4 ${isOverdue ? 'text-destructive' : 'text-primary'}`} />
          </div>
          <h4 className="font-medium text-sm">{log.truckNumber}</h4>
        </div>
        <span className={`text-xs font-medium px-2 py-1 rounded-full ${isOverdue ? 'bg-destructive text-destructive-foreground' : 'bg-muted text-muted-foreground'}`}>
          {isOverdue ? 'OVERDUE' : 'UPCOMING'}
        </span>
      </div>
      
      <p className="text-sm font-semibold mb-1">{log.serviceType}</p>
      <div className="flex items-center gap-2 text-xs text-muted-foreground">
        <Calendar className="w-3 h-3" />
        <span>Due: {new Date(log.dueDate).toLocaleDateString()}</span>
      </div>
    </div>
  );
};
