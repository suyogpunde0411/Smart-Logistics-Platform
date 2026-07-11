import React from 'react';
import { User, Truck, DollarSign, CheckCircle, XCircle, Map } from 'lucide-react';
import { motion } from 'framer-motion';

export const BidCard = ({ bid, onAccept, onReject }) => {
  return (
    <motion.div 
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-5 bg-card rounded-xl border border-border shadow-sm"
    >
      <div className="flex justify-between items-start mb-4">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center">
            <User className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h4 className="font-semibold">{bid.driverName || 'Driver'}</h4>
            <p className="text-xs text-muted-foreground flex items-center gap-1">
              Rating: {bid.driverRating || '4.5'} <span className="text-yellow-500">?</span>
            </p>
          </div>
        </div>
        <div className="text-right">
          <p className="text-lg font-bold text-foreground">${bid.bidAmount}</p>
          <p className="text-xs text-muted-foreground">Match Score: {bid.matchScore}%</p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-3 mb-5 text-sm text-muted-foreground">
        <div className="flex items-center gap-2">
          <Truck className="w-4 h-4" />
          <span>{bid.truckType || 'Heavy Truck'}</span>
        </div>
        <div className="flex items-center gap-2">
          <Map className="w-4 h-4" />
          <span>ETA: {bid.eta || '2 Days'}</span>
        </div>
      </div>

      {bid.status === 'PENDING' && (
        <div className="flex gap-3">
          <button 
            onClick={() => onAccept(bid.id)}
            className="flex-1 py-2 bg-primary text-primary-foreground rounded-md text-sm font-medium hover:bg-primary/90 flex justify-center items-center gap-2 transition-colors"
          >
            <CheckCircle className="w-4 h-4" /> Accept
          </button>
          <button 
            onClick={() => onReject(bid.id)}
            className="flex-1 py-2 bg-muted text-muted-foreground border border-border rounded-md text-sm font-medium hover:bg-muted/80 flex justify-center items-center gap-2 transition-colors"
          >
            <XCircle className="w-4 h-4" /> Reject
          </button>
        </div>
      )}
      
      {bid.status === 'ACCEPTED' && (
        <div className="w-full py-2 bg-green-500/10 text-green-600 rounded-md text-sm font-medium text-center border border-green-500/20">
          Bid Accepted
        </div>
      )}
    </motion.div>
  );
};
