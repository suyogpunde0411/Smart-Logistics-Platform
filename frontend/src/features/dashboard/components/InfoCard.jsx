import React from 'react';

export const InfoCard = ({ title, description, icon: Icon }) => (
  <div className="p-4 bg-card rounded-lg border border-border flex items-start gap-4 shadow-sm">
    {Icon && <div className="p-2 bg-muted rounded-md"><Icon className="w-5 h-5 text-primary" /></div>}
    <div>
      <h4 className="text-sm font-semibold">{title}</h4>
      <p className="text-xs text-muted-foreground mt-1">{description}</p>
    </div>
  </div>
);
