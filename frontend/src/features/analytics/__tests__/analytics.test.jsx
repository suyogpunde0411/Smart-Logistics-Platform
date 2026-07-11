import React from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { KPICard } from '../components/KPICard';
import { ExportButton } from '../components/ExportButton';
import { ChartContainer } from '../components/ChartContainer';

// Mock Recharts ResponsiveContainer to render children cleanly in jsdom environment
vi.mock('recharts', async () => {
  const original = await vi.importActual('recharts');
  return {
    ...original,
    ResponsiveContainer: ({ children }) => <div className="mock-responsive-container">{children}</div>,
  };
});

describe('Analytics & Charting Components', () => {
  it('renders KPICard with localized currency values', () => {
    render(<KPICard label="Revenue" value={142000} type="currency" />);
    expect(screen.getByText('Revenue')).toBeInTheDocument();
    expect(screen.getByText('₹1,42,000')).toBeInTheDocument();
  });

  it('renders KPICard with percent suffix', () => {
    render(<KPICard label="Success Rate" value={98.4} type="percent" />);
    expect(screen.getByText('Success Rate')).toBeInTheDocument();
    expect(screen.getByText('98.4%')).toBeInTheDocument();
  });

  it('toggles dropdown when export button is clicked', () => {
    const mockExportCsv = vi.fn();
    render(<ExportButton onExportCsv={mockExportCsv} label="Export Summary" />);
    
    const button = screen.getByText('Export Summary');
    expect(button).toBeInTheDocument();
    
    // Dropdown is closed initially
    expect(screen.queryByText('Export CSV')).not.toBeInTheDocument();
    
    // Click button to open
    fireEvent.click(button);
    expect(screen.getByText('Export CSV')).toBeInTheDocument();
    
    // Click Export CSV item
    fireEvent.click(screen.getByText('Export CSV'));
    expect(mockExportCsv).toHaveBeenCalled();
  });

  it('renders ChartContainer with correct title and no-data states', () => {
    render(<ChartContainer title="Metric distribution" data={[]} />);
    expect(screen.getByText('Metric distribution')).toBeInTheDocument();
    expect(screen.getByText('No analytics data is available for this period.')).toBeInTheDocument();
  });
});
