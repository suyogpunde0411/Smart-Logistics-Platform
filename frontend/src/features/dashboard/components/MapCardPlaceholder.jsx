import React from 'react';
import { BaseMap } from '@/components/common/BaseMap';

export const MapCardPlaceholder = ({ title, height = 300 }) => (
  <div className="p-6 bg-card rounded-xl border border-border shadow-sm flex flex-col h-full">
    <div className="mb-4">
      <h3 className="text-lg font-semibold">{title || 'Live Map'}</h3>
      <p className="text-sm text-muted-foreground">Location Tracking Placeholder</p>
    </div>
    <div className="flex-1 w-full relative rounded-lg overflow-hidden border border-border" style={{ minHeight: height }}>
      <BaseMap center={[51.505, -0.09]} zoom={13} height="100%" />
    </div>
  </div>
);
