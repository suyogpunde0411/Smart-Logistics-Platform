import React from 'react';
import { Network, CheckCircle, XCircle, Clock } from 'lucide-react';

export const MatchCard = ({ match }) => {
  const getStatusColor = (status) => {
    switch(status) {
      case 'ACCEPTED': return 'text-green-500 bg-green-500/10';
      case 'REJECTED': return 'text-red-500 bg-red-500/10';
      default: return 'text-yellow-500 bg-yellow-500/10';
    }
  };

  const StatusIcon = match.status === 'ACCEPTED' ? CheckCircle : match.status === 'REJECTED' ? XCircle : Clock;

  return (
    <div className="p-4 bg-card rounded-lg border border-border flex items-center justify-between">
      <div className="flex items-center gap-4">
        <div className="p-2 bg-primary/10 rounded-full">
          <Network className="w-5 h-5 text-primary" />
        </div>
        <div>
          <h4 className="font-medium text-sm">Match for Shipment #{match.shipmentId}</h4>
          <p className="text-xs text-muted-foreground">Score: {match.matchScore}% • Bid: </p>
        </div>
      </div>
      <div className={px-2.5 py-1 rounded-full text-xs font-medium flex items-center gap-1 }>
        <StatusIcon className="w-3 h-3" />
        {match.status}
      </div>
    </div>
  );
};
