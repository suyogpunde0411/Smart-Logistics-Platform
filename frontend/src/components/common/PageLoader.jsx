import React from 'react';
import { Loader2 } from 'lucide-react';

export const PageLoader = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-[400px] w-full p-8 animate-in fade-in duration-300">
      <Loader2 className="w-10 h-10 text-primary animate-spin mb-4" />
      <p className="text-sm font-medium text-muted-foreground animate-pulse">Loading platform assets...</p>
    </div>
  );
};

