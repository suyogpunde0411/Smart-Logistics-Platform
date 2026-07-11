import React from 'react';

export const SectionTitle = ({ title, subtitle }) => (
  <div className="mb-4">
    <h2 className="text-lg font-semibold">{title}</h2>
    {subtitle && <p className="text-sm text-muted-foreground">{subtitle}</p>}
  </div>
);
