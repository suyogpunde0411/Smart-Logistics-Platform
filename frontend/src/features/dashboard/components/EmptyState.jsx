import React from 'react';
import { FileQuestion } from 'lucide-react';

export const EmptyState = ({ title, description, action }) => (
  <div className="flex flex-col items-center justify-center p-8 text-center bg-card rounded-xl border border-dashed border-border">
    <div className="p-4 bg-muted rounded-full mb-4">
      <FileQuestion className="w-8 h-8 text-muted-foreground" />
    </div>
    <h3 className="text-lg font-medium">{title}</h3>
    <p className="text-sm text-muted-foreground max-w-sm mt-1 mb-6">{description}</p>
    {action}
  </div>
);
