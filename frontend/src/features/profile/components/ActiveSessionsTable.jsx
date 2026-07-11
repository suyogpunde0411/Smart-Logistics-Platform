import React from 'react';
import { Laptop, Smartphone, Monitor, LogOut } from 'lucide-react';
import { useActiveSessions, useRevokeSession } from '../hooks/useSecurity';

const getDeviceIcon = (deviceType) => {
  if (deviceType.toLowerCase().includes('mobile') || deviceType.toLowerCase().includes('phone')) return <Smartphone className="w-5 h-5 text-muted-foreground" />;
  if (deviceType.toLowerCase().includes('mac') || deviceType.toLowerCase().includes('windows')) return <Laptop className="w-5 h-5 text-muted-foreground" />;
  return <Monitor className="w-5 h-5 text-muted-foreground" />;
};

export const ActiveSessionsTable = () => {
  const { data: sessions, isLoading } = useActiveSessions();
  const { mutate: revokeSession, isPending } = useRevokeSession();

  // Mock data fallback
  const mockSessions = sessions || [
    { id: 'sess_1', device: 'MacBook Pro - Chrome', os: 'macOS', location: 'Mumbai, India', lastActive: 'Current Session', isCurrent: true },
    { id: 'sess_2', device: 'iPhone 13 - Safari', os: 'iOS', location: 'Pune, India', lastActive: '2 hours ago', isCurrent: false },
    { id: 'sess_3', device: 'Windows PC - Edge', os: 'Windows 11', location: 'Delhi, India', lastActive: 'Yesterday', isCurrent: false },
  ];

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading active sessions...</div>;

  return (
    <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
      <div className="p-6 border-b border-border bg-muted/10">
        <h3 className="font-semibold text-lg">Active Sessions</h3>
        <p className="text-sm text-muted-foreground mt-1">Review the devices currently logged into your account. Revoke any unfamiliar sessions.</p>
      </div>

      <div className="divide-y divide-border">
        {mockSessions.map((session) => (
          <div key={session.id} className="p-4 sm:p-6 flex flex-col sm:flex-row sm:items-center justify-between gap-4 transition-colors hover:bg-muted/30">
            <div className="flex items-start gap-4">
              <div className="p-3 bg-muted rounded-full shrink-0">
                {getDeviceIcon(session.device)}
              </div>
              <div>
                <div className="flex items-center gap-2 mb-1">
                  <h4 className="font-semibold text-sm">{session.device}</h4>
                  {session.isCurrent && (
                    <span className="px-2 py-0.5 text-[10px] font-bold bg-green-500/10 text-green-600 rounded-full border border-green-500/20 uppercase">This Device</span>
                  )}
                </div>
                <div className="text-sm text-muted-foreground flex items-center gap-2 flex-wrap">
                  <span>{session.location}</span>
                  <span className="w-1 h-1 bg-border rounded-full"></span>
                  <span>{session.os}</span>
                  <span className="w-1 h-1 bg-border rounded-full"></span>
                  <span className={session.isCurrent ? "text-primary font-medium" : ""}>{session.lastActive}</span>
                </div>
              </div>
            </div>

            {!session.isCurrent && (
              <button 
                onClick={() => revokeSession(session.id)}
                disabled={isPending}
                className="flex items-center justify-center gap-2 px-3 py-1.5 text-sm font-medium text-destructive hover:bg-destructive/10 rounded-lg transition-colors border border-transparent hover:border-destructive/20 disabled:opacity-50"
              >
                <LogOut className="w-4 h-4" /> Revoke
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};
