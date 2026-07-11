import React from 'react';
import { Area, AreaChart, Bar, BarChart, CartesianGrid, Cell, Line, LineChart, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis, Legend } from 'recharts';

const colors = ['#2563eb', '#10b981', '#f59e0b', '#8b5cf6', '#ef4444', '#06b6d4'];

export const ChartContainer = ({ title, data, type = 'line', dataKey = 'value', xKey = 'name', description, stackedKeys = [] }) => {
  const isHeatmap = type === 'heatmap';

  return (
    <section className="min-h-[350px] rounded-xl border border-border bg-card p-5 shadow-sm">
      <div className="mb-4">
        <h2 className="font-semibold text-lg text-foreground">{title}</h2>
        {description && <p className="mt-1 text-sm text-muted-foreground">{description}</p>}
      </div>

      {!data?.length && !isHeatmap ? (
        <div className="grid h-64 place-items-center text-sm text-muted-foreground">
          No analytics data is available for this period.
        </div>
      ) : (
        <div className="h-64" role="img" aria-label={title}>
          {isHeatmap ? (
            <div className="w-full h-full flex flex-col items-center justify-center bg-muted/20 border border-dashed border-border rounded-lg p-4">
              <div className="grid grid-cols-7 gap-2 w-full max-w-md">
                {Array.from({ length: 28 }).map((_, i) => {
                  const intensity = [100, 200, 350, 500, 700, 900][i % 6];
                  return (
                    <div 
                      key={i} 
                      className={`h-8 rounded transition-all hover:scale-105 cursor-pointer`}
                      style={{
                        backgroundColor: `rgba(37, 99, 235, ${intensity / 1000})`
                      }}
                      title={`Activity index: ${intensity}`}
                    />
                  );
                })}
              </div>
              <p className="text-xs text-muted-foreground mt-4">Density heat map placeholder for peak delivery hours.</p>
            </div>
          ) : (
            <ResponsiveContainer width="100%" height="100%">
              {type === 'bar' ? (
                <BarChart data={data}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(229, 231, 235, 0.5)" />
                  <XAxis dataKey={xKey} stroke="#888888" fontSize={12} tickLine={false} />
                  <YAxis stroke="#888888" fontSize={12} tickLine={false} />
                  <Tooltip cursor={{ fill: 'rgba(229, 231, 235, 0.2)' }} />
                  <Bar dataKey={dataKey} fill={colors[0]} radius={[4, 4, 0, 0]} />
                </BarChart>
              ) : type === 'stacked-bar' ? (
                <BarChart data={data}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(229, 231, 235, 0.5)" />
                  <XAxis dataKey={xKey} stroke="#888888" fontSize={12} tickLine={false} />
                  <YAxis stroke="#888888" fontSize={12} tickLine={false} />
                  <Tooltip />
                  <Legend />
                  {stackedKeys.map((key, index) => (
                    <Bar key={key} dataKey={key} stackId="a" fill={colors[index % colors.length]} />
                  ))}
                </BarChart>
              ) : type === 'area' ? (
                <AreaChart data={data}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(229, 231, 235, 0.5)" />
                  <XAxis dataKey={xKey} stroke="#888888" fontSize={12} tickLine={false} />
                  <YAxis stroke="#888888" fontSize={12} tickLine={false} />
                  <Tooltip />
                  <Area type="monotone" dataKey={dataKey} stroke={colors[0]} fill={colors[0]} fillOpacity={0.15} strokeWidth={2} />
                </AreaChart>
              ) : type === 'donut' ? (
                <PieChart>
                  <Pie data={data} dataKey={dataKey} nameKey={xKey} innerRadius={55} outerRadius={80} paddingAngle={2}>
                    {data.map((item, index) => (
                      <Cell key={item[xKey] ?? index} fill={colors[index % colors.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              ) : type === 'pie' ? (
                <PieChart>
                  <Pie data={data} dataKey={dataKey} nameKey={xKey} outerRadius={80}>
                    {data.map((item, index) => (
                      <Cell key={item[xKey] ?? index} fill={colors[index % colors.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              ) : (
                <LineChart data={data}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(229, 231, 235, 0.5)" />
                  <XAxis dataKey={xKey} stroke="#888888" fontSize={12} tickLine={false} />
                  <YAxis stroke="#888888" fontSize={12} tickLine={false} />
                  <Tooltip />
                  <Line type="monotone" dataKey={dataKey} stroke={colors[0]} strokeWidth={3} dot={false} />
                </LineChart>
              )}
            </ResponsiveContainer>
          )}
        </div>
      )}
    </section>
  );
};
