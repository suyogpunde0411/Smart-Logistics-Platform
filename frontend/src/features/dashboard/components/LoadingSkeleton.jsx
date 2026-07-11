import React from 'react';

export const LoadingSkeleton = ({ count = 1, className = "h-24" }) => (
  <div className="space-y-4 w-full">
    {Array.from({ length: count }).map((_, i) => (
      <div key={i} className={g-muted animate-pulse rounded-md w-full } />
    ))}
  </div>
);
