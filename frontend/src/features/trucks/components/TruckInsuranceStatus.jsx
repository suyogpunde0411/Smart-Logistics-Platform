import React from 'react';
import { ShieldAlert, ShieldCheck, Clock } from 'lucide-react';

export const TruckInsuranceStatus = ({ insurance }) => {
  if (!insurance) {
    return (
      <div className="p-6 border border-destructive/20 bg-destructive/5 rounded-xl flex items-start gap-4">
        <ShieldAlert className="w-8 h-8 text-destructive shrink-0" />
        <div>
          <h4 className="font-semibold text-destructive">No Active Insurance</h4>
          <p className="text-sm text-destructive/80 mt-1">This truck cannot be dispatched until valid insurance is uploaded and verified.</p>
        </div>
      </div>
    );
  }

  // Very basic date check for mockup purposes
  const expiryDate = new Date(insurance.expiryDate);
  const daysUntilExpiry = Math.ceil((expiryDate - new Date()) / (1000 * 60 * 60 * 24));
  
  const isExpiringSoon = daysUntilExpiry <= 30 && daysUntilExpiry > 0;
  const isExpired = daysUntilExpiry <= 0;

  return (
    <div className={`p-6 border rounded-xl flex items-start gap-4 ${
      isExpired ? 'border-destructive/20 bg-destructive/5' : 
      isExpiringSoon ? 'border-yellow-500/20 bg-yellow-500/5' : 
      'border-green-500/20 bg-green-500/5'
    }`}>
      {isExpired ? (
        <ShieldAlert className="w-8 h-8 text-destructive shrink-0" />
      ) : isExpiringSoon ? (
        <Clock className="w-8 h-8 text-yellow-600 shrink-0" />
      ) : (
        <ShieldCheck className="w-8 h-8 text-green-600 shrink-0" />
      )}
      
      <div className="w-full">
        <div className="flex justify-between items-start">
          <h4 className={`font-semibold ${
            isExpired ? 'text-destructive' : 
            isExpiringSoon ? 'text-yellow-700' : 
            'text-green-700'
          }`}>
            {insurance.provider} - {insurance.policyNumber}
          </h4>
          <span className={`text-xs font-bold px-2 py-1 rounded-full ${
            isExpired ? 'bg-destructive/10 text-destructive' : 
            isExpiringSoon ? 'bg-yellow-500/10 text-yellow-700' : 
            'bg-green-500/10 text-green-700'
          }`}>
            {isExpired ? 'EXPIRED' : isExpiringSoon ? 'EXPIRING SOON' : 'ACTIVE'}
          </span>
        </div>
        
        <div className="grid grid-cols-2 gap-4 mt-4 text-sm">
          <div>
            <p className="text-muted-foreground mb-1">Issue Date</p>
            <p className="font-medium">{insurance.issueDate}</p>
          </div>
          <div>
            <p className="text-muted-foreground mb-1">Expiry Date</p>
            <p className={`font-medium ${isExpiringSoon || isExpired ? 'text-destructive' : ''}`}>
              {insurance.expiryDate} 
              {isExpiringSoon && ` (${daysUntilExpiry} days left)`}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
