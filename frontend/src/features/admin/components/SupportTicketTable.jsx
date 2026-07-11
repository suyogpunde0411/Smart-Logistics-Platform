import React from 'react';
import { CheckCircle, Clock } from 'lucide-react';

export const SupportTicketTable = ({ tickets, onResolve }) => {
  if (!tickets?.length) return <div className="p-8 text-center text-muted-foreground border rounded-xl">No active tickets.</div>;

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
      <table className="w-full text-sm text-left">
        <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
          <tr>
            <th className="px-6 py-4 font-medium">Ticket ID</th>
            <th className="px-6 py-4 font-medium">Subject</th>
            <th className="px-6 py-4 font-medium">Submitted By</th>
            <th className="px-6 py-4 font-medium">Status</th>
            <th className="px-6 py-4 font-medium text-right">Action</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-border">
          {tickets.map((t) => (
            <tr key={t.id} className="hover:bg-muted/30 transition-colors">
              <td className="px-6 py-4 font-medium text-foreground">#{t.id.substring(0,8)}</td>
              <td className="px-6 py-4">{t.subject}</td>
              <td className="px-6 py-4 text-muted-foreground">{t.submittedBy}</td>
              <td className="px-6 py-4">
                <span className={`px-2.5 py-1 text-xs font-medium rounded-full flex items-center w-fit gap-1 ${t.status === 'OPEN' ? 'bg-yellow-500/10 text-yellow-600' : 'bg-green-500/10 text-green-600'}`}>
                  {t.status === 'OPEN' ? <Clock className="w-3 h-3" /> : <CheckCircle className="w-3 h-3" />}
                  {t.status}
                </span>
              </td>
              <td className="px-6 py-4 text-right">
                {t.status === 'OPEN' && (
                  <button onClick={() => onResolve(t.id)} className="text-xs font-medium bg-primary/10 text-primary px-3 py-1.5 rounded hover:bg-primary hover:text-primary-foreground transition-colors">
                    Mark Resolved
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
