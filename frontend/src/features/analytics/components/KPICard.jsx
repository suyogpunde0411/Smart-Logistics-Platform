import React from 'react';
import { TrendingDown, TrendingUp } from 'lucide-react';

const formatValue = (value, type) => {
  if (value === null || value === undefined) return '—';
  if (type === 'currency') return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(value);
  if (type === 'percent') return `${Number(value).toFixed(1)}%`;
  return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 1 }).format(value);
};

export const KPICard = ({ label, value, type, icon: Icon, trend }) => (
  <article className="rounded-xl border border-border bg-card p-5 shadow-sm" aria-label={`${label}: ${formatValue(value, type)}`}>
    <div className="flex items-start justify-between gap-3">
      <div><p className="text-sm text-muted-foreground">{label}</p><p className="mt-2 text-2xl font-semibold tracking-tight">{formatValue(value, type)}</p></div>
      {Icon && <span className="rounded-lg bg-primary/10 p-2 text-primary"><Icon size={20} aria-hidden="true" /></span>}
    </div>
    {trend !== undefined && <p className={`mt-3 flex items-center gap-1 text-xs ${trend >= 0 ? 'text-emerald-600' : 'text-rose-600'}`}>{trend >= 0 ? <TrendingUp size={14} /> : <TrendingDown size={14} />}{Math.abs(trend)}% from prior period</p>}
  </article>
);
