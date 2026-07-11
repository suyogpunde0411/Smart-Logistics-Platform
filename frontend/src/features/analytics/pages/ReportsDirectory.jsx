import React from 'react';
import { useNavigate } from 'react-router-dom';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { FileText, Truck, CircleDollarSign, Users, BarChart3, ShieldAlert, Award } from 'lucide-react';

const reportsList = [
  { id: 'trip', title: 'Trip Report', desc: 'Granular logs of all completed trips, durations, ETAs, and routes.', icon: <Truck className="w-6 h-6 text-blue-500" /> },
  { id: 'shipment', title: 'Shipment Report', desc: 'Overview of shipment lifecycle states, successful deliveries, and cancellation stats.', icon: <FileText className="w-6 h-6 text-green-500" /> },
  { id: 'revenue', title: 'Revenue Report', desc: 'Full billing information, gross platform margin, margins, and spending trends.', icon: <CircleDollarSign className="w-6 h-6 text-amber-500" /> },
  { id: 'fleet', title: 'Fleet Report', desc: 'Fleet utilization, truck efficiency, fuel metrics placeholder, and maintenance history.', icon: <BarChart3 className="w-6 h-6 text-purple-500" /> },
  { id: 'driver', title: 'Driver Report', desc: 'Performance ratings, trip counts, acceptance rates, and average ratings.', icon: <Users className="w-6 h-6 text-pink-500" /> },
  { id: 'business', title: 'Business Report', desc: 'Customer stats, volume of shipments registered, and monthly spend patterns.', icon: <Award className="w-6 h-6 text-cyan-500" /> },
  { id: 'matching', title: 'Matching Report', desc: 'Bidding efficiencies, contract acceptances, and matching success rates.', icon: <ShieldAlert className="w-6 h-6 text-indigo-500" /> },
  { id: 'carbon', title: 'Carbon Savings Report', desc: 'Eco-impact metrics, green mileage metrics, and estimated CO2 emission reductions.', icon: <Award className="w-6 h-6 text-emerald-500" /> },
];

export const ReportsDirectory = () => {
  const navigate = useNavigate();

  return (
    <PageContainer>
      <div className="mb-6">
        <DashboardHeader 
          title="On-Demand Reports" 
          description="Select a category below to filter, analyze, and download detailed spreadsheet or document summaries." 
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {reportsList.map((report) => (
          <div 
            key={report.id} 
            onClick={() => navigate(`/reports/${report.id}`)}
            className="group flex flex-col justify-between p-6 bg-card border border-border rounded-xl shadow-sm hover:shadow-md hover:border-primary/50 cursor-pointer transition-all duration-200"
          >
            <div>
              <div className="p-3 bg-muted rounded-lg w-fit mb-4 group-hover:bg-primary/10 transition-colors">
                {report.icon}
              </div>
              <h3 className="font-semibold text-lg text-foreground group-hover:text-primary transition-colors mb-2">{report.title}</h3>
              <p className="text-sm text-muted-foreground leading-relaxed">{report.desc}</p>
            </div>
            <div className="mt-6 flex items-center text-sm font-medium text-primary group-hover:translate-x-1 transition-transform">
              View Detailed Log &rarr;
            </div>
          </div>
        ))}
      </div>
    </PageContainer>
  );
};
