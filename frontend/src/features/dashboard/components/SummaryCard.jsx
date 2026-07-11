import React from 'react';

export const SummaryCard = ({ title, value, subtext }) => (
  <div className="p-4 bg-primary/10 rounded-lg border border-primary/20">
    <h4 className="text-sm font-medium text-primary mb-1">{title}</h4>
    <p className="text-2xl font-bold text-foreground">{value}</p>
    {subtext && <p className="text-xs text-muted-foreground mt-1">{subtext}</p>}
  </div>
);
