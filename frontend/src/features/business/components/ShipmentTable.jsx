import React from 'react';
import { Package, MoreVertical, Eye } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const ShipmentTable = ({ shipments, isLoading }) => {
  const navigate = useNavigate();

  if (isLoading) return <div className="p-8 text-center text-muted-foreground animate-pulse">Loading shipments...</div>;
  if (!shipments || shipments.length === 0) return <div className="p-8 text-center text-muted-foreground">No shipments found.</div>;

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
      <table className="w-full text-sm text-left">
        <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
          <tr>
            <th className="px-6 py-4 font-medium">Tracking ID</th>
            <th className="px-6 py-4 font-medium">Route</th>
            <th className="px-6 py-4 font-medium">Date</th>
            <th className="px-6 py-4 font-medium">Cargo Type</th>
            <th className="px-6 py-4 font-medium">Status</th>
            <th className="px-6 py-4 font-medium text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-border">
          {shipments.map((s) => (
            <tr key={s.id} className="hover:bg-muted/30 transition-colors">
              <td className="px-6 py-4 font-medium text-foreground flex items-center gap-2">
                <Package className="w-4 h-4 text-muted-foreground" />
                {s.trackingId || s.id.substring(0,8)}
              </td>
              <td className="px-6 py-4">
                <span className="truncate max-w-[150px] inline-block">{s.pickupLocation}</span>
                <span className="mx-2 text-muted-foreground">&rarr;</span>
                <span className="truncate max-w-[150px] inline-block">{s.deliveryLocation}</span>
              </td>
              <td className="px-6 py-4 text-muted-foreground">{new Date(s.pickupDate).toLocaleDateString()}</td>
              <td className="px-6 py-4">{s.cargoType}</td>
              <td className="px-6 py-4">
                <span className="px-2.5 py-1 text-xs font-medium rounded-full bg-primary/10 text-primary">
                  {s.status}
                </span>
              </td>
              <td className="px-6 py-4 text-right">
                <button 
                  onClick={() => navigate('/shipments/' + s.id)}
                  className="p-2 text-muted-foreground hover:text-primary hover:bg-muted rounded-md transition-colors"
                  title="View Details"
                >
                  <Eye className="w-4 h-4" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
