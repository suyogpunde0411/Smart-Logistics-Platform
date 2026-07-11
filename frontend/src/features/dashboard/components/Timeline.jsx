import React from 'react';

export const Timeline = ({ items }) => (
  <div className="relative border-l border-muted-foreground/30 ml-3 space-y-6">
    {items?.map((item, index) => (
      <div key={index} className="relative pl-6">
        <span className="absolute -left-1.5 top-1.5 w-3 h-3 rounded-full bg-primary ring-4 ring-background" />
        <h4 className="text-sm font-medium">{item.title}</h4>
        <p className="text-xs text-muted-foreground">{item.date}</p>
        {item.description && <p className="text-sm mt-1">{item.description}</p>}
      </div>
    ))}
  </div>
);
