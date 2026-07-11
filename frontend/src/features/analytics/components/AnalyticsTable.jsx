import React from 'react';
const labelFor = (key) => key.replace(/([A-Z])/g, ' $1').replace(/^./, (letter) => letter.toUpperCase());
export const AnalyticsTable = ({ rows = [] }) => {
  const columns = [...new Set(rows.flatMap((row) => Object.keys(row)))].slice(0, 6);
  if (!rows.length) return <div className="rounded-xl border border-border bg-card p-8 text-center text-sm text-muted-foreground">No trip records match the selected filters.</div>;
  return <div className="overflow-x-auto rounded-xl border border-border bg-card"><table className="w-full text-left text-sm"><thead className="border-b border-border bg-muted/40"><tr>{columns.map((key) => <th key={key} className="whitespace-nowrap px-4 py-3 font-medium">{labelFor(key)}</th>)}</tr></thead><tbody>{rows.map((row, index) => <tr key={row.id ?? row.tripId ?? index} className="border-b border-border last:border-0">{columns.map((key) => <td key={key} className="whitespace-nowrap px-4 py-3 text-muted-foreground">{String(row[key] ?? '—')}</td>)}</tr>)}</tbody></table></div>;
};
