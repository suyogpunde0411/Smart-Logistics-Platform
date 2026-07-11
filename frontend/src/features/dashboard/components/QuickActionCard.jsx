import React from 'react';
import { motion } from 'framer-motion';

export const QuickActionCard = ({ title, description, icon: Icon, onClick }) => (
  <motion.button
    whileHover={{ scale: 1.02 }}
    whileTap={{ scale: 0.98 }}
    onClick={onClick}
    className="p-4 w-full text-left bg-card hover:bg-muted/50 rounded-lg border border-border shadow-sm flex flex-col items-center justify-center text-center space-y-2 transition-colors"
  >
    {Icon && <div className="p-3 bg-primary/10 text-primary rounded-full"><Icon className="w-6 h-6" /></div>}
    <h4 className="text-sm font-semibold">{title}</h4>
    {description && <p className="text-xs text-muted-foreground">{description}</p>}
  </motion.button>
);
