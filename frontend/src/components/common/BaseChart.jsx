import React from 'react';
import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

export const BaseChart = ({ data, lines, xKey }) => {
  return (
    <div className="w-full h-64 bg-card rounded-lg p-4 shadow-sm">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" className="stroke-border" />
          <XAxis dataKey={xKey} className="text-xs text-muted-foreground" />
          <YAxis className="text-xs text-muted-foreground" />
          <Tooltip 
            contentStyle={{ backgroundColor: 'hsl(var(--popover))', borderColor: 'hsl(var(--border))' }}
            itemStyle={{ color: 'hsl(var(--foreground))' }}
          />
          <Legend />
          {lines.map((line, index) => (
            <Line 
              key={index} 
              type="monotone" 
              dataKey={line.dataKey} 
              stroke={line.color || 'hsl(var(--primary))'} 
              strokeWidth={2}
            />
          ))}
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};
