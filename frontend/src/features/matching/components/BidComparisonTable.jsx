import React from 'react';
import { Star, ShieldCheck, CheckCircle, XCircle } from 'lucide-react';
import { useAcceptBid } from '../../shipments/hooks/useShipmentMatches'; // Reuse the hook we built in shipments

export const BidComparisonTable = ({ shipmentId, bids = [] }) => {
  const { mutate: acceptBid, isPending } = useAcceptBid();

  if (!bids.length) return <div className="p-8 text-center text-muted-foreground border rounded-xl">No bids to compare.</div>;

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-border bg-card shadow-sm">
      <table className="w-full text-sm text-left">
        <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
          <tr>
            <th className="px-6 py-4 font-medium">Provider</th>
            <th className="px-6 py-4 font-medium">Vehicle</th>
            <th className="px-6 py-4 font-medium">Trust & Reliability</th>
            <th className="px-6 py-4 font-medium text-right">Bid Amount</th>
            <th className="px-6 py-4 font-medium text-center">Action</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-border">
          {bids.map((bid) => (
            <tr key={bid.id} className="hover:bg-muted/30 transition-colors">
              <td className="px-6 py-4">
                <p className="font-semibold">{bid.fleetOwnerName}</p>
                <p className="text-xs text-muted-foreground mt-0.5">Driver: {bid.driverName || 'TBD'}</p>
              </td>
              <td className="px-6 py-4">
                <p className="font-medium">{bid.truckType}</p>
                <p className="text-xs text-muted-foreground mt-0.5">{bid.truckRegistration}</p>
              </td>
              <td className="px-6 py-4">
                <div className="flex flex-col gap-1">
                  <div className="flex items-center gap-1 text-yellow-600 font-medium">
                    <Star className="w-3.5 h-3.5 fill-current" /> {bid.rating} ({bid.totalTrips || 120} Trips)
                  </div>
                  {bid.isVerified && (
                    <div className="flex items-center gap-1 text-green-600 text-xs">
                      <ShieldCheck className="w-3.5 h-3.5" /> Verified Provider
                    </div>
                  )}
                </div>
              </td>
              <td className="px-6 py-4 text-right">
                <p className="font-bold text-lg">₹{bid.amount}</p>
                <p className="text-xs text-muted-foreground mt-0.5">ETA: {bid.estimatedPickup}</p>
              </td>
              <td className="px-6 py-4 text-center">
                <div className="flex items-center justify-center gap-2">
                  <button 
                    onClick={() => acceptBid({ shipmentId, bidId: bid.id })}
                    disabled={isPending || bid.status === 'REJECTED'}
                    className="flex items-center justify-center gap-1 bg-green-600 hover:bg-green-700 text-white px-3 py-1.5 rounded-lg font-medium transition-colors disabled:opacity-50"
                  >
                    <CheckCircle className="w-4 h-4" /> Accept
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
