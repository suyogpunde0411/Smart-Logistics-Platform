import React, { useState } from 'react';
import { X, DollarSign, Clock, AlertCircle } from 'lucide-react';
import { useSubmitBid } from '../hooks/useBidManagement';

export const BidSubmissionModal = ({ shipment, isOpen, onClose }) => {
  const [amount, setAmount] = useState(shipment?.budget?.replace(/[^0-9]/g, '') || '');
  const [expiryHours, setExpiryHours] = useState('24');
  const { mutate: submitBid, isPending } = useSubmitBid();

  if (!isOpen || !shipment) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    submitBid(
      { shipmentId: shipment.id, bidData: { amount: Number(amount), expiryHours: Number(expiryHours) } },
      { onSuccess: () => onClose() }
    );
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-background/80 backdrop-blur-sm">
      <div className="bg-card border border-border shadow-lg rounded-xl w-full max-w-md p-6 relative animate-in zoom-in-95">
        <button onClick={onClose} className="absolute right-4 top-4 text-muted-foreground hover:text-foreground">
          <X className="w-5 h-5" />
        </button>
        
        <h2 className="text-xl font-bold mb-1">Place Bid</h2>
        <p className="text-sm text-muted-foreground mb-6">Shipment: {shipment.name} ({shipment.origin} to {shipment.destination})</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="text-sm font-medium mb-1 block">Your Bid Amount (₹)</label>
            <div className="relative">
              <DollarSign className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input 
                type="number" 
                required
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                className="w-full pl-9 pr-4 py-2 border border-input rounded-md bg-background focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>
            <p className="text-xs text-muted-foreground mt-1">Client budget is {shipment.budget}</p>
          </div>

          <div>
            <label className="text-sm font-medium mb-1 block">Bid Valid For (Hours)</label>
            <div className="relative">
              <Clock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <select 
                value={expiryHours}
                onChange={(e) => setExpiryHours(e.target.value)}
                className="w-full pl-9 pr-4 py-2 border border-input rounded-md bg-background focus:outline-none focus:ring-2 focus:ring-primary"
              >
                <option value="6">6 Hours</option>
                <option value="12">12 Hours</option>
                <option value="24">24 Hours</option>
                <option value="48">48 Hours</option>
              </select>
            </div>
          </div>

          <div className="bg-yellow-500/10 border border-yellow-500/20 text-yellow-700 p-3 rounded-md flex gap-2 items-start mt-4">
            <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
            <p className="text-xs">Once placed, the Business Owner will be notified. If accepted, you will be bound to this rate.</p>
          </div>

          <div className="pt-4 flex gap-3 justify-end">
            <button type="button" onClick={onClose} className="px-4 py-2 border border-border rounded-md text-sm font-medium hover:bg-muted transition-colors">
              Cancel
            </button>
            <button type="submit" disabled={isPending || !amount} className="px-6 py-2 bg-primary text-primary-foreground rounded-md text-sm font-medium hover:bg-primary/90 transition-colors disabled:opacity-50">
              {isPending ? 'Submitting...' : 'Submit Bid'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
