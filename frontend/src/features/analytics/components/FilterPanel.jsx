import React from 'react';
import { DateRangePicker } from './DateRangePicker';

export const FilterPanel = ({ period, onPeriodChange, onApply, canFilterByRegion = false }) => (
  <div className="flex flex-col gap-3 rounded-xl border border-border bg-card p-4 md:flex-row md:items-center md:justify-between">
    <DateRangePicker value={period} onChange={onPeriodChange} />
    <div className="flex gap-2">{canFilterByRegion && <input aria-label="Region" className="w-36 rounded-md border border-input bg-background px-3 py-1.5 text-sm" placeholder="Region" disabled title="Region data is not exposed by Analytics Service yet" />}<button type="button" onClick={onApply} className="rounded-md bg-primary px-3 py-1.5 text-sm font-medium text-primary-foreground">Apply filters</button></div>
  </div>
);
