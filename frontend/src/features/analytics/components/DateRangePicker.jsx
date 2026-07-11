import React from 'react';

export const DateRangePicker = ({ value, onChange }) => (
  <div className="flex flex-wrap items-center gap-2" aria-label="Date range">
    {['today', 'week', 'month', 'year'].map((period) => <button key={period} type="button" onClick={() => onChange(period)} className={`rounded-md px-3 py-1.5 text-sm capitalize ${value === period ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground hover:text-foreground'}`}>{period === 'week' ? 'This week' : period === 'month' ? 'This month' : period === 'year' ? 'This year' : 'Today'}</button>)}
  </div>
);
