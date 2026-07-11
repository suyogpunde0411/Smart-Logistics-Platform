import React from 'react';
import { ToggleLeft, ToggleRight } from 'lucide-react';

export const FeatureFlagPanel = ({ settings, onToggle }) => {
  const flags = [
    { key: 'MAINTENANCE_MODE', label: 'Global Maintenance Mode', desc: 'Lock the platform for all non-admins.' },
    { key: 'ALLOW_REGISTRATION', label: 'User Registration', desc: 'Allow new users to sign up.' },
    { key: 'AUTO_MATCHING', label: 'AI Auto-Matching', desc: 'Enable automated matching algorithm for shipments.' }
  ];

  return (
    <div className="bg-card rounded-xl border border-border shadow-sm p-6">
      <h3 className="text-lg font-semibold mb-6">System Feature Flags</h3>
      <div className="space-y-6">
        {flags.map(f => {
          const isActive = settings?.[f.key] === true;
          return (
            <div key={f.key} className="flex items-center justify-between">
              <div>
                <p className="font-medium">{f.label}</p>
                <p className="text-sm text-muted-foreground">{f.desc}</p>
              </div>
              <button 
                onClick={() => onToggle(f.key, !isActive)}
                className={`p-2 rounded-full transition-colors ${isActive ? 'text-primary' : 'text-muted-foreground'}`}
              >
                {isActive ? <ToggleRight className="w-8 h-8" /> : <ToggleLeft className="w-8 h-8" />}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};
