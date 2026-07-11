import React from 'react';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { Activity, Star, Truck, Package, Clock, ShieldCheck } from 'lucide-react';
import { MatchScoreIndicator } from '../components/MatchScoreIndicator';

export const MatchDashboard = () => {
  const { currentRole } = useSelector((state) => state.auth);
  const navigate = useNavigate();

  // Determine if user is supply (driver/fleet) or demand (business)
  const isSupply = currentRole === 'Driver' || currentRole === 'Fleet Owner';
  const isAdmin = currentRole === 'Admin' || currentRole === 'Super Admin';

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader 
          title={isAdmin ? 'Platform Matching Engine' : 'Matching Dashboard'} 
          description={isSupply ? 'Find the best shipments for your trucks.' : isAdmin ? 'Global matching statistics and health.' : 'Find reliable trucks for your shipments.'} 
        />
        {!isAdmin && (
          <button 
            onClick={() => navigate('/matching/recommendations')}
            className="px-4 py-2 bg-primary text-primary-foreground rounded-lg font-medium hover:bg-primary/90 transition-colors"
          >
            {isSupply ? 'View Recommended Shipments' : 'Find Available Trucks'}
          </button>
        )}
      </div>

      {/* Global Analytics (For Admins) */}
      {isAdmin && (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <div className="flex justify-between items-start mb-2">
              <p className="text-sm font-medium text-muted-foreground">Match Success Rate</p>
              <Activity className="w-4 h-4 text-primary" />
            </div>
            <h3 className="text-2xl font-bold">84.2%</h3>
            <p className="text-xs text-green-600 mt-1">↑ 2.1% from last month</p>
          </div>
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <div className="flex justify-between items-start mb-2">
              <p className="text-sm font-medium text-muted-foreground">Average Match Score</p>
              <Star className="w-4 h-4 text-yellow-500" />
            </div>
            <h3 className="text-2xl font-bold">92/100</h3>
            <p className="text-xs text-muted-foreground mt-1">Highly Optimized</p>
          </div>
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <div className="flex justify-between items-start mb-2">
              <p className="text-sm font-medium text-muted-foreground">Pending Matches</p>
              <Clock className="w-4 h-4 text-blue-500" />
            </div>
            <h3 className="text-2xl font-bold">142</h3>
            <p className="text-xs text-muted-foreground mt-1">Currently negotiating</p>
          </div>
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <div className="flex justify-between items-start mb-2">
              <p className="text-sm font-medium text-muted-foreground">Avg. Time to Match</p>
              <Activity className="w-4 h-4 text-primary" />
            </div>
            <h3 className="text-2xl font-bold">14m 30s</h3>
            <p className="text-xs text-green-600 mt-1">↓ 1m 15s faster</p>
          </div>
        </div>
      )}

      {/* User-Specific Quick Stats */}
      {!isAdmin && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm flex items-center gap-4">
            <div className="p-4 bg-primary/10 rounded-full text-primary"><Star className="w-6 h-6" /></div>
            <div>
              <p className="text-sm font-medium text-muted-foreground">Your Match Rating</p>
              <h3 className="text-xl font-bold">Excellent (94%)</h3>
            </div>
          </div>
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm flex items-center gap-4">
            <div className="p-4 bg-blue-500/10 rounded-full text-blue-600"><Clock className="w-6 h-6" /></div>
            <div>
              <p className="text-sm font-medium text-muted-foreground">Active Bids</p>
              <h3 className="text-xl font-bold">4 Pending</h3>
            </div>
          </div>
          <div className="bg-card border border-border rounded-xl p-6 shadow-sm flex items-center gap-4">
            <div className="p-4 bg-green-500/10 rounded-full text-green-600"><ShieldCheck className="w-6 h-6" /></div>
            <div>
              <p className="text-sm font-medium text-muted-foreground">Matches Completed</p>
              <h3 className="text-xl font-bold">128</h3>
            </div>
          </div>
        </div>
      )}

      {/* Recent Activity */}
      <div className="bg-card border border-border rounded-xl shadow-sm">
        <div className="p-6 border-b border-border">
          <h3 className="font-semibold text-lg">{isAdmin ? 'Recent Global Matches' : 'Your Recent Matches'}</h3>
        </div>
        <div className="p-6 text-center text-muted-foreground">
          <MatchScoreIndicator score={88} size="lg" />
          <p className="mt-4">The AI Engine is continuously scanning for optimal pairings.</p>
          {!isAdmin && (
            <button 
              onClick={() => navigate('/matching/recommendations')}
              className="mt-6 px-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors"
            >
              Browse Recommendations
            </button>
          )}
        </div>
      </div>
    </PageContainer>
  );
};
