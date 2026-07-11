import React from 'react';
import { motion } from 'framer-motion';

export const StatsCard = ({ title, value, icon: Icon, trend, description }) => {
  return (
    <motion.div 
      whileHover={{ y: -2 }}
      className="p-6 bg-card rounded-xl border border-border shadow-sm flex flex-col space-y-4"
    >
      <div className="flex items-center justify-between">
        <p className="text-sm font-medium text-muted-foreground">{title}</p>
        {Icon && <Icon className="w-5 h-5 text-muted-foreground" />}
      </div>
      <div>
        <h3 className="text-2xl font-bold">{value}</h3>
        {description && (
          <p className="text-xs text-muted-foreground mt-1">
            {trend && (
              <span className={`mr-1 font-medium ${trend > 0 ? 'text-green-500' : 'text-red-500'}`}>
                {trend > 0 ? '+' : ''}{trend}%
              </span>
            )}
            {description}
          </p>
        )}
      </div>
    </motion.div>
  );
};
