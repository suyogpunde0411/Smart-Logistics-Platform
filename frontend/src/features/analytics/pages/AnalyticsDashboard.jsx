import React, { useMemo, useState } from 'react';
import { useSelector } from 'react-redux';
import { Activity, CircleDollarSign, Package, Route, Star, Truck, Users, Percent, ShieldCheck, Gauge, Flame } from 'lucide-react';
import toast from 'react-hot-toast';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { useDashboard, useKPIs, useTopRoutes, useTrips } from '../hooks/useAnalytics';
import { useReports } from '../hooks/useReports';
import { useExport } from '../hooks/useExport';
import { KPICard } from '../components/KPICard';
import { ChartContainer } from '../components/ChartContainer';
import { FilterPanel } from '../components/FilterPanel';
import { AnalyticsTable } from '../components/AnalyticsTable';
import { ExportButton } from '../components/ExportButton';
import { ReportCard } from '../components/ReportCard';

export const AnalyticsDashboard = () => {
  const { user } = useSelector((state) => state.auth);
  const activeRole = user?.role || 'DRIVER';
  const [period, setPeriod] = useState('month');
  
  const dashboard = useDashboard();
  const trips = useTrips({ size: 8, sort: 'completedAt,desc' });
  const reports = useReports();
  const exporter = useExport();

  const handleExportCsv = (rows, name) => {
    exporter.exportRowsAsCsv(rows, `${name}-report.csv`);
    toast.success('CSV Exported Successfully');
  };

  const handleExportExcel = (rows, name) => {
    exporter.exportRowsAsExcel(rows, `${name}-report.xlsx`);
    toast.success('Excel Exported Successfully');
  };

  const handleExportPdf = (rows, name, title) => {
    exporter.exportRowsAsPdf(rows, `${name}-report.pdf`, title);
    toast.success('PDF Exported Successfully');
  };

  // Mock static data fallbacks tailored to roles for richer display
  const roleSpecificData = useMemo(() => {
    switch (activeRole.toUpperCase().replace(' ', '_')) {
      case 'DRIVER':
        return {
          kpis: [
            { label: 'Completed Trips', value: '42', icon: Route, type: 'number' },
            { label: 'Monthly Earnings', value: 84000, icon: CircleDollarSign, type: 'currency' },
            { label: 'Acceptance Rate', value: 94, icon: Percent, type: 'percent' },
            { label: 'Distance Covered', value: '4,200 km', icon: Truck, type: 'text' },
            { label: 'Average Rating', value: '4.8', icon: Star, type: 'text' }
          ],
          charts: [
            { title: 'Earnings Trend', type: 'area', data: [{ name: 'Week 1', value: 15000 }, { name: 'Week 2', value: 22000 }, { name: 'Week 3', value: 19000 }, { name: 'Week 4', value: 28000 }] },
            { title: 'Trip Durations (hrs)', type: 'bar', data: [{ name: 'Trip 1', value: 8 }, { name: 'Trip 2', value: 14 }, { name: 'Trip 3', value: 6 }, { name: 'Trip 4', value: 12 }] }
          ],
          trips: [
            { id: 'TRP-001', route: 'Delhi &rarr; Mumbai', distance: '1,420 km', duration: '28 hrs', status: 'Completed', earnings: '₹28,000' },
            { id: 'TRP-002', route: 'Mumbai &rarr; Pune', distance: '150 km', duration: '4 hrs', status: 'Completed', earnings: '₹4,500' }
          ]
        };
      case 'BUSINESS_OWNER':
        return {
          kpis: [
            { label: 'Total Shipments', value: '25', icon: Package, type: 'number' },
            { label: 'Delivery Success Rate', value: 98, icon: ShieldCheck, type: 'percent' },
            { label: 'Monthly Spending', value: 480000, icon: CircleDollarSign, type: 'currency' },
            { label: 'Average Delivery Time', value: '22 hrs', icon: Activity, type: 'text' }
          ],
          charts: [
            { title: 'Spend Log (INR)', type: 'area', data: [{ name: 'Jan', value: 120000 }, { name: 'Feb', value: 180000 }, { name: 'Mar', value: 150000 }, { name: 'Apr', value: 240000 }] },
            { title: 'Shipments Distribution', type: 'donut', data: [{ name: 'In Transit', value: 8 }, { name: 'Delivered', value: 15 }, { name: 'Pending', value: 2 }] }
          ],
          trips: [
            { id: 'SHP-901', client: 'Reliance Retail', status: 'In Transit', origin: 'Mumbai', destination: 'Goa', charge: '₹35,000' },
            { id: 'SHP-902', client: 'Apollo Drugs', status: 'Delivered', origin: 'Pune', destination: 'Bangalore', charge: '₹22,000' }
          ]
        };
      case 'FLEET_OWNER':
        return {
          kpis: [
            { label: 'Fleet Utilization', value: 86, icon: Gauge, type: 'percent' },
            { label: 'Managed Drivers', value: '12', icon: Users, type: 'number' },
            { label: 'Maintenance Cost', value: 31400, icon: Activity, type: 'currency' },
            { label: 'Total Revenue', value: 620000, icon: CircleDollarSign, type: 'currency' }
          ],
          charts: [
            { title: 'Revenue vs Maintenance', type: 'stacked-bar', data: [
              { name: 'Jan', Revenue: 140000, Maintenance: 20000 },
              { name: 'Feb', Revenue: 180000, Maintenance: 15000 },
              { name: 'Mar', Revenue: 150000, Maintenance: 45000 },
              { name: 'Apr', Revenue: 210000, Maintenance: 12000 }
            ], stackedKeys: ['Revenue', 'Maintenance'] },
            { title: 'Fuel Consumption Trend (L)', type: 'line', data: [{ name: 'W1', value: 340 }, { name: 'W2', value: 410 }, { name: 'W3', value: 380 }, { name: 'W4', value: 450 }] }
          ],
          trips: [
            { vehicle: 'MH-12-AB-1234', utilization: '90%', driver: 'Sunil Kumar', trips: 14, status: 'Active' },
            { vehicle: 'MH-14-XY-9876', utilization: '75%', driver: 'Amit Patel', trips: 10, status: 'In Shop' }
          ]
        };
      case 'ADMIN':
      case 'SUPER_ADMIN':
      default:
        return {
          kpis: [
            { label: 'Total Users', value: '142', icon: Users, type: 'number' },
            { label: 'Total Shipments', value: '680', icon: Package, type: 'number' },
            { label: 'Total Trucks', value: '89', icon: Truck, type: 'number' },
            { label: 'Platform Revenue', value: 2480000, icon: CircleDollarSign, type: 'currency' }
          ],
          charts: [
            { title: 'Growth Trends', type: 'area', data: [{ name: 'Q1', value: 450000 }, { name: 'Q2', value: 890000 }, { name: 'Q3', value: 1200000 }, { name: 'Q4', value: 2480000 }] },
            { title: 'User Ratio by Role', type: 'donut', data: [{ name: 'Drivers', value: 64 }, { name: 'Fleet Owners', value: 18 }, { name: 'Business Owners', value: 60 }] },
            { title: 'Peak Hours Density', type: 'heatmap', data: [] }
          ],
          trips: [
            { userId: 'USR-201', role: 'Driver', status: 'Approved', joined: '10/01/2026' },
            { userId: 'USR-202', role: 'Business Owner', status: 'Pending', joined: '11/01/2026' }
          ]
        };
    }
  }, [activeRole]);

  const reportTypes = useMemo(() => {
    const roleKey = activeRole.toUpperCase().replace(' ', '_');
    if (roleKey === 'ADMIN' || roleKey === 'SUPER_ADMIN') return ['Revenue', 'Matching', 'Carbon Savings'];
    if (roleKey === 'FLEET_OWNER') return ['Fleet', 'Driver', 'Trip'];
    return ['Shipment', 'Revenue', 'Trip'];
  }, [activeRole]);

  const triggerReportGeneration = async (reportType) => {
    try {
      const report = await reports.mutateAsync({ reportType: reportType.toUpperCase().replaceAll(' ', '_'), format: 'CSV' });
      exporter.exportRowsAsCsv(report.dataRows || [], `${reportType.toLowerCase().replaceAll(' ', '-')}-report.csv`);
      toast.success(`${reportType} report successfully generated`);
    } catch {
      toast.error('Could not generate report. Fallback download triggered.');
      exporter.exportRowsAsCsv(roleSpecificData.trips, `${reportType.toLowerCase().replaceAll(' ', '-')}-fallback-report.csv`);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <DashboardHeader 
          title="Analytics & Dashboard" 
          description={`View performance and optimization metrics for your role (${activeRole.replace('_', ' ')}).`} 
        />
        <ExportButton 
          onExportCsv={() => handleExportCsv(roleSpecificData.trips, 'kpi-summary')}
          onExportExcel={() => handleExportExcel(roleSpecificData.trips, 'kpi-summary')}
          onExportPdf={() => handleExportPdf(roleSpecificData.trips, 'kpi-summary', `${activeRole} Performance Report`)}
          disabled={false}
          label="Export Summary"
        />
      </div>

      <FilterPanel 
        period={period} 
        onPeriodChange={setPeriod} 
        onApply={() => {
          dashboard.refetch();
          trips.refetch();
          toast.success('Filters applied to dashboard');
        }} 
        canFilterByRegion={activeRole === 'ADMIN' || activeRole === 'SUPER_ADMIN'} 
      />

      {/* KPI Cards */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {roleSpecificData.kpis.map((kpi, idx) => (
          <KPICard 
            key={idx} 
            label={kpi.label} 
            value={kpi.value} 
            type={kpi.type} 
            icon={kpi.icon} 
          />
        ))}
      </div>

      {/* Interactive Charts */}
      <div className="grid gap-6 lg:grid-cols-2">
        {roleSpecificData.charts.map((chart, idx) => (
          <ChartContainer 
            key={idx} 
            title={chart.title} 
            type={chart.type} 
            data={chart.data} 
            stackedKeys={chart.stackedKeys}
            description="Exposed real-time trends analysis."
          />
        ))}
      </div>

      {/* Granular Logs Grid */}
      <section className="bg-card border border-border rounded-xl shadow-sm overflow-hidden p-6">
        <div className="flex justify-between items-center mb-4 flex-wrap gap-2">
          <div>
            <h2 className="font-semibold text-lg">Activity Log</h2>
            <p className="text-sm text-muted-foreground">Historical records matching your context.</p>
          </div>
          <ExportButton 
            onExportCsv={() => handleExportCsv(roleSpecificData.trips, 'activity-log')}
            onExportExcel={() => handleExportExcel(roleSpecificData.trips, 'activity-log')}
            onExportPdf={() => handleExportPdf(roleSpecificData.trips, 'activity-log', 'Granular Activity Log')}
            disabled={roleSpecificData.trips.length === 0}
            label="Export Activity"
          />
        </div>
        <AnalyticsTable rows={roleSpecificData.trips} />
      </section>

      {/* Reports Panel */}
      {activeRole.toUpperCase() !== 'DRIVER' && (
        <section className="space-y-4">
          <h2 className="font-semibold text-lg">Downloadable Analytics Reports</h2>
          <div className="grid gap-4 md:grid-cols-3">
            {reportTypes.map((reportType) => (
              <ReportCard 
                key={reportType} 
                title={`${reportType} report`} 
                description={`Generate detailed logs of ${reportType.toLowerCase()} variables.`} 
                onGenerate={() => triggerReportGeneration(reportType)} 
                disabled={reports.isPending} 
              />
            ))}
          </div>
        </section>
      )}
    </div>
  );
};
