import React from 'react';
import { ShieldCheck, Trash2, EyeOff } from 'lucide-react';

export const ReviewModerationTable = ({ reviews, onModerate }) => {
  if (!reviews?.length) return <div className="p-8 text-center text-muted-foreground border rounded-xl">No reported reviews pending moderation.</div>;

  return (
    <div className="w-full overflow-x-auto rounded-xl border border-border bg-card">
      <table className="w-full text-sm text-left">
        <thead className="bg-muted/50 border-b border-border text-muted-foreground uppercase text-xs">
          <tr>
            <th className="px-6 py-4 font-medium">Review Content</th>
            <th className="px-6 py-4 font-medium">Report Reason</th>
            <th className="px-6 py-4 font-medium">Author</th>
            <th className="px-6 py-4 font-medium text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-border">
          {reviews.map((r) => (
            <tr key={r.id} className="hover:bg-muted/30 transition-colors">
              <td className="px-6 py-4 max-w-[300px] truncate" title={r.comment}>
                <div className="font-medium">Rating: {r.rating}★</div>
                <div className="text-muted-foreground text-xs mt-1 truncate">{r.comment}</div>
              </td>
              <td className="px-6 py-4 text-destructive text-xs font-medium">{r.reportReason || 'Inappropriate Content'}</td>
              <td className="px-6 py-4 text-muted-foreground">{r.authorName}</td>
              <td className="px-6 py-4 text-right flex justify-end gap-2">
                <button onClick={() => onModerate(r.id, 'APPROVE')} className="p-2 text-muted-foreground hover:text-green-600 hover:bg-green-500/10 rounded-md transition-colors" title="Keep Review">
                  <ShieldCheck className="w-4 h-4" />
                </button>
                <button onClick={() => onModerate(r.id, 'HIDE')} className="p-2 text-muted-foreground hover:text-yellow-600 hover:bg-yellow-500/10 rounded-md transition-colors" title="Hide Review">
                  <EyeOff className="w-4 h-4" />
                </button>
                <button onClick={() => onModerate(r.id, 'DELETE')} className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-md transition-colors" title="Delete Review">
                  <Trash2 className="w-4 h-4" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
