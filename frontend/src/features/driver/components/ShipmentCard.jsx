import React from 'react';
import { Package, MapPin, Calendar, DollarSign } from 'lucide-react';
import { motion } from 'framer-motion';

export const ShipmentCard = ({ shipment, onRequestMatch }) => {
  return (
    <motion.div 
      whileHover={{ y: -2 }}
      className="p-5 bg-card rounded-xl border border-border shadow-sm flex flex-col"
    >
      <div className="flex justify-between items-start mb-4">
        <div className="flex items-center gap-2">
          <div className="p-2 bg-primary/10 rounded-md">
            <Package className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h3 className="font-semibold text-foreground">{shipment.title || 'Standard Shipment'}</h3>
            <p className="text-xs text-muted-foreground">{shipment.weight} kg • {shipment.type}</p>
          </div>
        </div>
        <span className="px-2.5 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full dark:bg-green-900/30 dark:text-green-400">
          Available
        </span>
      </div>

      <div className="space-y-2 mb-6 flex-1">
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <MapPin className="w-4 h-4 flex-shrink-0" />
          <span className="truncate">{shipment.pickupLocation} &rarr; {shipment.deliveryLocation}</span>
        </div>
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <Calendar className="w-4 h-4 flex-shrink-0" />
          <span>{new Date(shipment.pickupDate).toLocaleDateString()}</span>
        </div>
        <div className="flex items-center gap-2 text-sm font-medium text-foreground">
          <DollarSign className="w-4 h-4 flex-shrink-0 text-muted-foreground" />
          <span></span>
        </div>
      </div>

      <button 
        onClick={() => onRequestMatch(shipment.id)}
        className="w-full py-2 bg-primary text-primary-foreground rounded-md text-sm font-medium hover:bg-primary/90 transition-colors"
      >
        Request Match
      </button>
    </motion.div>
  );
};
