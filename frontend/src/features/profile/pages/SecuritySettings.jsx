import React, { useState } from 'react';
import { SecurityCard } from '../components/SecurityCard';
import { ActiveSessionsTable } from '../components/ActiveSessionsTable';
import { ShieldAlert, Trash2 } from 'lucide-react';
import { useDeleteAccountRequest } from '../hooks/useSecurity';

export const SecuritySettings = () => {
  const { mutate: requestDeletion, isPending } = useDeleteAccountRequest();
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [deleteReason, setDeleteReason] = useState('');

  const handleDeleteRequest = () => {
    if (!deleteReason.trim()) return;
    requestDeletion(deleteReason, {
      onSuccess: () => {
        setShowDeleteConfirm(false);
        setDeleteReason('');
      }
    });
  };

  return (
    <div className="space-y-8 max-w-4xl">
      <SecurityCard />
      
      <ActiveSessionsTable />

      {/* Danger Zone */}
      <div className="bg-destructive/5 border border-destructive/20 rounded-xl shadow-sm overflow-hidden mt-12">
        <div className="p-6">
          <h3 className="font-semibold text-lg text-destructive flex items-center gap-2">
            <ShieldAlert className="w-5 h-5" /> Danger Zone
          </h3>
          <p className="text-sm text-muted-foreground mt-2 max-w-2xl">
            Permanently deleting your account will erase all your personal data, trip history, and active shipments. This action cannot be undone. For security purposes, an admin will review the request.
          </p>
          
          {!showDeleteConfirm ? (
            <button 
              onClick={() => setShowDeleteConfirm(true)}
              className="mt-6 px-4 py-2 border border-destructive text-destructive hover:bg-destructive hover:text-destructive-foreground rounded-lg font-medium transition-colors"
            >
              Request Account Deletion
            </button>
          ) : (
            <div className="mt-6 p-4 bg-background border border-destructive/20 rounded-lg">
              <label className="block text-sm font-medium mb-2 text-foreground">Why are you leaving?</label>
              <textarea 
                value={deleteReason}
                onChange={(e) => setDeleteReason(e.target.value)}
                placeholder="Please tell us why you are requesting account deletion..."
                className="w-full px-3 py-2 bg-muted/50 border border-input rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-destructive resize-none mb-3"
                rows={3}
              />
              <div className="flex gap-3">
                <button 
                  onClick={handleDeleteRequest}
                  disabled={isPending || !deleteReason.trim()}
                  className="flex items-center gap-2 px-4 py-2 bg-destructive text-destructive-foreground rounded-lg font-medium hover:bg-destructive/90 transition-colors disabled:opacity-50"
                >
                  {isPending ? 'Submitting...' : <><Trash2 className="w-4 h-4" /> Submit Request</>}
                </button>
                <button 
                  onClick={() => setShowDeleteConfirm(false)}
                  disabled={isPending}
                  className="px-4 py-2 bg-muted text-muted-foreground hover:text-foreground rounded-lg font-medium transition-colors"
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
