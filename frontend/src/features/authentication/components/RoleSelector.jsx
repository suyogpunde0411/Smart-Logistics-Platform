import React from 'react';
import { Truck, Building2, Building } from 'lucide-react';

export const RoleSelector = ({ value, onChange, error }) => {
  const roles = [
    { id: 'Driver', label: 'Driver', icon: Truck },
    { id: 'Business Owner', label: 'Business Owner', icon: Building },
    { id: 'Fleet Owner', label: 'Fleet Owner', icon: Building2 },
  ];

  return (
    <div className="space-y-2">
      <label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
        Select Role
      </label>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {roles.map((role) => {
          const Icon = role.icon;
          const isSelected = value === role.id;
          return (
            <button
              key={role.id}
              type="button"
              onClick={() => onChange(role.id)}
              className={`flex flex-col items-center justify-center p-4 rounded-lg border-2 transition-all ${
                isSelected
                  ? 'border-primary bg-primary/10 text-primary'
                  : 'border-muted hover:border-primary/50 text-muted-foreground hover:text-foreground'
              }`}
            >
              <Icon className="w-8 h-8 mb-2" />
              <span className="text-sm font-medium">{role.label}</span>
            </button>
          );
        })}
      </div>
      {error && <p className="text-sm text-destructive mt-1">{error}</p>}
    </div>
  );
};
