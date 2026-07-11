import React from 'react';
import { Clock, Navigation2, Activity } from 'lucide-react';

export const ETAWidget = ({ etaData, isLive = true }) => {
  if (!etaData) return null;

  return (
    <div className="bg-primary text-primary-foreground rounded-xl p-6 shadow-md relative overflow-hidden">
      {/* Background Decorative Graphic */}
      <div className="absolute -right-6 -top-6 text-primary-foreground/10">
        <Activity className="w-32 h-32" />
      </div>

      <div className="relative z-10">
        <div className="flex items-center gap-2 mb-6">
          <div className={`w-2 h-2 rounded-full ${isLive ? 'bg-green-400 animate-pulse' : 'bg-primary-foreground/50'}`}></div>
          <span className="text-sm font-medium opacity-90">{isLive ? 'LIVE TRACKING' : 'OFFLINE'}</span>
        </div>

        <div className="grid grid-cols-2 gap-8">
          <div>
            <p className="text-primary-foreground/70 text-sm font-medium mb-1 flex items-center gap-2">
              <Clock className="w-4 h-4" /> Estimated Arrival
            </p>
            <h2 className="text-3xl font-bold">{etaData.estimatedTime}</h2>
            <p className="text-sm mt-1 opacity-90">{etaData.estimatedDate}</p>
          </div>

          <div>
            <p className="text-primary-foreground/70 text-sm font-medium mb-1 flex items-center gap-2">
              <Navigation2 className="w-4 h-4" /> Distance Remaining
            </p>
            <h2 className="text-3xl font-bold">{etaData.remainingDistance} km</h2>
            <p className="text-sm mt-1 opacity-90">of {etaData.totalDistance} km total</p>
          </div>
        </div>

        {/* Progress Bar */}
        <div className="mt-6">
          <div className="flex justify-between text-xs font-medium opacity-80 mb-2">
            <span>{etaData.coveredDistance} km covered</span>
            <span>{Math.round((etaData.coveredDistance / etaData.totalDistance) * 100)}%</span>
          </div>
          <div className="w-full h-2 bg-primary-foreground/20 rounded-full overflow-hidden">
            <div 
              className="h-full bg-white rounded-full transition-all duration-1000 ease-in-out" 
              style={{ width: `${(etaData.coveredDistance / etaData.totalDistance) * 100}%` }}
            ></div>
          </div>
        </div>
      </div>
    </div>
  );
};
