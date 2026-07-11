import React from 'react';
import { Star, Truck, User, DollarSign, Clock, CheckCircle, XCircle } from 'lucide-react';
import { useAcceptBid } from '../hooks/useShipmentMatches';

export const ShipmentMatchBoard = ({ shipmentId, bids = [] }) => {
  const { mutate: acceptBid, isPending } = useAcceptBid();

  if (!bids.length) {
    return <div className="p-8 text-center text-muted-foreground border rounded-xl bg-card">No bids or matches found yet. Waiting for fleet owners to respond.</div>;
  }

  return (
    <div className="space-y-4">
      {bids.map((bid) => (
        <div key={bid.id} className="bg-card border border-border rounded-xl p-6 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
          
          <div className="flex gap-4">
            <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center shrink-0">
              <Truck className="w-6 h-6 text-primary" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h4 className="font-semibold text-lg">{bid.fleetOwnerName}</h4>
                <span className="flex items-center text-xs font-medium bg-yellow-500/10 text-yellow-700 px-2 py-0.5 rounded-full">
                  <Star className="w-3 h-3 mr-1 fill-current" /> {bid.rating}
                </span>
              </div>
              <p className="text-sm text-muted-foreground mt-1">Truck: {bid.truckType} • {bid.truckRegistration}</p>
              {bid.driverName && <p className="text-sm text-muted-foreground">Driver: {bid.driverName}</p>}
            </div>
          </div>

          <div className="flex flex-col sm:flex-row gap-6 w-full md:w-auto">
            <div>
              <p className="text-sm text-muted-foreground mb-1 flex items-center gap-1"><DollarSign className="w-4 h-4" /> Bid Amount</p>
              <p className="font-semibold text-lg text-foreground">₹{bid.amount}</p>
            </div>
            <div>
              <p className="text-sm text-muted-foreground mb-1 flex items-center gap-1"><Clock className="w-4 h-4" /> Est. Pickup</p>
              <p className="font-medium text-foreground">{bid.estimatedPickup}</p>
            </div>
            
            <div className="flex items-center gap-2 mt-2 sm:mt-0">
              <button 
                onClick={() => acceptBid({ shipmentId, bidId: bid.id })}
                disabled={isPending || bid.status === 'REJECTED'}
                className="flex-1 sm:flex-none flex items-center justify-center gap-2 bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-medium transition-colors disabled:opacity-50"
              >
                <CheckCircle className="w-4 h-4" /> Accept
              </button>
              <button 
                disabled={isPending || bid.status === 'REJECTED'}
                className="flex items-center justify-center p-2 border border-border hover:bg-destructive/10 hover:text-destructive hover:border-destructive/30 rounded-lg transition-colors disabled:opacity-50"
                title="Reject Bid"
              >
                <XCircle className="w-5 h-5" />
              </button>
            </div>
          </div>

        </div>
      ))}
    </div>
  );
};
