import React from 'react';
import { Wrench, Calendar, DollarSign } from 'lucide-react';

export const TruckMaintenanceLog = ({ logs = [] }) => {
  if (!logs.length) return <div className="p-8 text-center text-muted-foreground border rounded-xl">No maintenance history available.</div>;

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
      <table className="w-full text-sm text-left">
        <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
          <tr>
            <th className="px-6 py-4 font-medium">Date</th>
            <th className="px-6 py-4 font-medium">Service Type</th>
            <th className="px-6 py-4 font-medium">Provider</th>
            <th className="px-6 py-4 font-medium text-right">Cost</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-border">
          {logs.map((log, idx) => (
            <tr key={idx} className="hover:bg-muted/30 transition-colors">
              <td className="px-6 py-4">
                <div className="flex items-center gap-2">
                  <Calendar className="w-4 h-4 text-muted-foreground" />
                  {log.date}
                </div>
              </td>
              <td className="px-6 py-4">
                <div className="flex items-center gap-2">
                  <Wrench className="w-4 h-4 text-muted-foreground" />
                  <span className="font-medium">{log.serviceType}</span>
                </div>
              </td>
              <td className="px-6 py-4 text-muted-foreground">{log.provider}</td>
              <td className="px-6 py-4 text-right">
                <div className="flex items-center justify-end gap-1 font-medium">
                  <DollarSign className="w-3 h-3 text-muted-foreground" />
                  {log.cost}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
