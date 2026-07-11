import React from 'react';
import { BaseChart } from '@/components/common/BaseChart';

export const ChartCard = ({ title, description, data, config, type = 'line', height = 300 }) => {
  return (
    <div className="p-6 bg-card rounded-xl border border-border shadow-sm flex flex-col h-full">
      <div className="mb-4">
        <h3 className="text-lg font-semibold">{title}</h3>
        {description && <p className="text-sm text-muted-foreground">{description}</p>}
      </div>
      <div className="flex-1 w-full" style={{ minHeight: height }}>
        <BaseChart data={data} config={config} type={type} height="100%" />
      </div>
    </div>
  );
};
