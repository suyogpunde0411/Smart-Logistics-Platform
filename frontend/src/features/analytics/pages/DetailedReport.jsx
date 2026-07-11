import React, { useState, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { AnalyticsTable } from '../components/AnalyticsTable';
import { ExportButton } from '../components/ExportButton';
import { FilterPanel } from '../components/FilterPanel';
import { useExport } from '../hooks/useExport';
import { ArrowLeft, FileSpreadsheet, Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';

export const DetailedReport = () => {
  const { reportType } = useParams();
  const navigate = useNavigate();
  const [period, setPeriod] = useState('month');
  const [region, setRegion] = useState('');
  const exporter = useExport();

  // Standardize the title
  const reportTitle = useMemo(() => {
    return reportType.charAt(0).toUpperCase() + reportType.slice(1) + ' Report';
  }, [reportType]);

  // Generates high quality mock data tailored to the report type
  const reportData = useMemo(() => {
    const defaultData = {
      trip: [
        { id: 'T-101', route: 'Mumbai &rarr; Delhi', vehicle: 'MH-12-AB-1234', driver: 'Sunil Kumar', duration: '28 hrs', status: 'Completed', distance: '1420 km' },
        { id: 'T-102', route: 'Pune &rarr; Bangalore', vehicle: 'MH-14-XY-9876', driver: 'Amit Patel', duration: '18 hrs', status: 'Completed', distance: '840 km' },
        { id: 'T-103', route: 'Chennai &rarr; Hyderabad', vehicle: 'TN-01-JK-5544', driver: 'Raju Naidu', duration: '12 hrs', status: 'Completed', distance: '620 km' },
        { id: 'T-104', route: 'Kolkata &rarr; Patna', vehicle: 'WB-02-ZZ-3321', driver: 'Sanjay Ghosh', duration: '15 hrs', status: 'Completed', distance: '580 km' },
      ],
      shipment: [
        { id: 'S-901', sender: 'Logistics India', recipient: 'Reliance Retail', status: 'Delivered', cost: '₹45,000', weight: '12 Tons', eta: 'On Time' },
        { id: 'S-902', sender: 'Tata Steel', recipient: 'L&T Construction', status: 'Delivered', cost: '₹1,20,000', weight: '24 Tons', eta: 'Delayed' },
        { id: 'S-903', sender: 'Pharma Chemicals', recipient: 'Apollo Drugs', status: 'Delivered', cost: '₹32,000', weight: '4 Tons', eta: 'On Time' },
      ],
      revenue: [
        { month: 'Jan 2026', totalRevenue: '₹12,40,000', driverPayouts: '₹8,90,000', platformMargin: '₹3,50,000', activeClients: '45', growth: '+12%' },
        { month: 'Feb 2026', totalRevenue: '₹14,80,000', driverPayouts: '₹10,20,000', platformMargin: '₹4,60,000', activeClients: '49', growth: '+19%' },
        { month: 'Mar 2026', totalRevenue: '₹16,50,000', driverPayouts: '₹11,80,000', platformMargin: '₹4,70,000', activeClients: '52', growth: '+11%' },
      ],
      fleet: [
        { truck: 'MH-12-AB-1234', type: 'Container 20ft', utilization: '84%', maintenanceCost: '₹12,500', fuelConsumed: '420L', distanceTravelled: '4,200 km' },
        { truck: 'MH-14-XY-9876', type: 'Flatbed 40ft', utilization: '78%', maintenanceCost: '₹18,900', fuelConsumed: '680L', distanceTravelled: '6,100 km' },
        { truck: 'KA-03-MM-4433', type: 'Reefer 20ft', utilization: '91%', maintenanceCost: '₹6,400', fuelConsumed: '390L', distanceTravelled: '3,800 km' },
      ],
      driver: [
        { name: 'Sunil Kumar', tripsCompleted: 42, activeHours: '168 hrs', acceptanceRate: '94%', averageRating: '4.8 ⭐', earnings: '₹84,000' },
        { name: 'Amit Patel', tripsCompleted: 38, activeHours: '152 hrs', acceptanceRate: '88%', averageRating: '4.6 ⭐', earnings: '₹76,000' },
        { name: 'Raju Naidu', tripsCompleted: 51, activeHours: '210 hrs', acceptanceRate: '96%', averageRating: '4.9 ⭐', earnings: '₹1,02,000' },
      ],
      business: [
        { clientName: 'Reliance Retail', activeShipments: 12, totalVolume: '84 Tons', grossSpend: '₹4,80,000', activeRoutes: '6', rating: '4.7 ⭐' },
        { clientName: 'L&T Construction', activeShipments: 5, totalVolume: '120 Tons', grossSpend: '₹8,50,000', activeRoutes: '3', rating: '4.5 ⭐' },
        { clientName: 'Apollo Drugs', activeShipments: 8, totalVolume: '22 Tons', grossSpend: '₹1,90,000', activeRoutes: '4', rating: '4.9 ⭐' },
      ],
      matching: [
        { shipmentId: 'S-901', originalBudget: '₹48,000', acceptedBid: '₹45,000', totalBidders: 4, matchTimeMinutes: 12, bidDelta: '-₹3,000' },
        { shipmentId: 'S-902', originalBudget: '₹1,30,000', acceptedBid: '₹1,20,000', totalBidders: 8, matchTimeMinutes: 24, bidDelta: '-₹10,000' },
        { shipmentId: 'S-903', originalBudget: '₹35,000', acceptedBid: '₹32,000', totalBidders: 3, matchTimeMinutes: 9, bidDelta: '-₹3,000' },
      ],
      carbon: [
        { month: 'Jan 2026', ecoTrips: '24', greenMileage: '12,400 km', avgEcoDrivingScore: '92/100', carbonPrevented: '1.8 Tons', fuelSaved: '680 Liters' },
        { month: 'Feb 2026', ecoTrips: '31', greenMileage: '15,800 km', avgEcoDrivingScore: '94/100', carbonPrevented: '2.4 Tons', fuelSaved: '910 Liters' },
        { month: 'Mar 2026', ecoTrips: '38', greenMileage: '18,200 km', avgEcoDrivingScore: '91/100', carbonPrevented: '2.9 Tons', fuelSaved: '1,120 Liters' },
      ],
    };

    return defaultData[reportType.toLowerCase()] || [];
  }, [reportType]);

  const handleExportCsv = () => {
    exporter.exportRowsAsCsv(reportData, `${reportType}-report.csv`);
    toast.success('Report successfully exported to CSV');
  };

  const handleExportExcel = () => {
    exporter.exportRowsAsExcel(reportData, `${reportType}-report.xlsx`);
    toast.success('Report successfully exported to Excel');
  };

  const handleExportPdf = () => {
    exporter.exportRowsAsPdf(reportData, `${reportType}-report.pdf`, reportTitle);
    toast.success('Report successfully exported to PDF');
  };

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <div className="flex items-center gap-3">
          <button 
            onClick={() => navigate('/reports')}
            className="p-2 border border-border bg-card rounded-lg hover:bg-muted transition-colors"
            aria-label="Back to reports"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <DashboardHeader 
            title={reportTitle} 
            description={`Granular on-demand metrics and trends analysis.`} 
          />
        </div>
        
        <ExportButton 
          onExportCsv={handleExportCsv}
          onExportExcel={handleExportExcel}
          onExportPdf={handleExportPdf}
          disabled={reportData.length === 0}
          label="Export Report"
        />
      </div>

      <div className="mb-6">
        <FilterPanel 
          period={period} 
          onPeriodChange={setPeriod} 
          onApply={() => toast.success('Filters applied to dataset')} 
          canFilterByRegion={true} 
        />
      </div>

      <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
        <div className="p-6 border-b border-border bg-muted/10 flex justify-between items-center">
          <div>
            <h3 className="font-semibold text-lg">Granular Records</h3>
            <p className="text-sm text-muted-foreground mt-0.5">Displaying current matches based on filters.</p>
          </div>
          <span className="text-xs font-semibold px-2.5 py-1 bg-primary/10 text-primary border border-primary/20 rounded-full">
            {reportData.length} records
          </span>
        </div>

        <AnalyticsTable rows={reportData} />
      </div>
    </PageContainer>
  );
};
