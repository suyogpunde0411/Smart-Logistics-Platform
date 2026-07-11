import React from 'react';
import { UserX, UserCheck, Eye } from 'lucide-react';

export const UserTable = ({ users, onStatusChange }) => {
  if (!users?.length) return <div className="p-8 text-center text-muted-foreground border rounded-xl">No users found.</div>;

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
      <table className="w-full text-sm text-left">
        <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
          <tr>
            <th className="px-6 py-4 font-medium">Name</th>
            <th className="px-6 py-4 font-medium">Role</th>
            <th className="px-6 py-4 font-medium">Email</th>
            <th className="px-6 py-4 font-medium">Status</th>
            <th className="px-6 py-4 font-medium text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-border">
          {users.map((u) => (
            <tr key={u.id} className="hover:bg-muted/30 transition-colors">
              <td className="px-6 py-4 font-medium text-foreground">{u.name || u.firstName + ' ' + u.lastName}</td>
              <td className="px-6 py-4">
                <span className="px-2 py-1 text-xs font-medium rounded bg-secondary text-secondary-foreground">
                  {u.role}
                </span>
              </td>
              <td className="px-6 py-4 text-muted-foreground">{u.email}</td>
              <td className="px-6 py-4">
                <span className={`px-2.5 py-1 text-xs font-medium rounded-full ${u.status === 'ACTIVE' ? 'bg-green-500/10 text-green-600' : 'bg-destructive/10 text-destructive'}`}>
                  {u.status}
                </span>
              </td>
              <td className="px-6 py-4 text-right flex justify-end gap-2">
                <button className="p-2 text-muted-foreground hover:text-primary hover:bg-muted rounded-md transition-colors" title="View Profile">
                  <Eye className="w-4 h-4" />
                </button>
                {u.status === 'ACTIVE' ? (
                  <button onClick={() => onStatusChange(u.id, 'SUSPENDED')} className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-md transition-colors" title="Suspend User">
                    <UserX className="w-4 h-4" />
                  </button>
                ) : (
                  <button onClick={() => onStatusChange(u.id, 'ACTIVE')} className="p-2 text-muted-foreground hover:text-green-600 hover:bg-green-500/10 rounded-md transition-colors" title="Activate User">
                    <UserCheck className="w-4 h-4" />
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
