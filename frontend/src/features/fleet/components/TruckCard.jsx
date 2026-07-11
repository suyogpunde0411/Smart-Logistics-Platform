import React from 'react';
import { Truck, MapPin, CheckCircle, Clock } from 'lucide-react';
import { motion } from 'framer-motion';

export const TruckCard = ({ truck }) => {
  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      className="p-5 bg-card rounded-xl border border-border shadow-sm flex flex-col"
    >
      <div className="flex justify-between items-start mb-4">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center">
            <Truck className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h4 className="font-semibold">{truck.registrationNumber}</h4>
            <p className="text-xs text-muted-foreground">{truck.truckType} • {truck.capacity} tons</p>
          </div>
        </div>
        <span className={`px-2.5 py-1 text-xs font-medium rounded-full ${
          truck.status === 'AVAILABLE' ? 'bg-green-500/10 text-green-600' :
          truck.status === 'IN_TRANSIT' ? 'bg-blue-500/10 text-blue-600' :
          'bg-yellow-500/10 text-yellow-600'
        }`}>
          {truck.status}
        </span>
      </div>

      <div className="mt-auto space-y-2 text-sm text-muted-foreground">
        <div className="flex items-center gap-2">
          <MapPin className="w-4 h-4" />
          <span>Current: {truck.currentLocation || 'Unknown'}</span>
        </div>
        <div className="flex items-center gap-2">
          {truck.driverId ? <CheckCircle className="w-4 h-4 text-green-500" /> : <Clock className="w-4 h-4 text-yellow-500" />}
          <span>Driver: {truck.driverName || 'Unassigned'}</span>
        </div>
      </div>
    </motion.div>
  );
};
