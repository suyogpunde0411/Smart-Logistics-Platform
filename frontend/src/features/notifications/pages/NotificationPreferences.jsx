import React, { useState, useEffect } from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { useNotificationPreferences, useUpdateNotificationPreferences } from '../hooks/useNotificationPreferences';
import { Bell, Smartphone, Mail, Save } from 'lucide-react';

export const NotificationPreferences = () => {
  const { data: preferences, isLoading } = useNotificationPreferences();
  const { mutate: updatePreferences, isPending } = useUpdateNotificationPreferences();

  // Local state for the form
  const [formData, setFormData] = useState({
    emailAlerts: true,
    smsAlerts: false,
    pushAlerts: true,
    categories: {
      TRIP_UPDATE: true,
      SHIPMENT_UPDATE: true,
      MATCHING: true,
      MAINTENANCE_ALERT: false,
      MARKETING: false,
      SYSTEM: true
    }
  });

  // Sync with backend if available
  useEffect(() => {
    if (preferences) setFormData(preferences);
  }, [preferences]);

  const handleToggle = (field, isCategory = false) => {
    if (isCategory) {
      setFormData(prev => ({
        ...prev,
        categories: { ...prev.categories, [field]: !prev.categories[field] }
      }));
    } else {
      setFormData(prev => ({ ...prev, [field]: !prev[field] }));
    }
  };

  const handleSave = () => {
    updatePreferences(formData);
  };

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading preferences...</div>;

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader 
          title="Notification Preferences" 
          description="Control how and when you receive alerts from the platform." 
        />
        <button 
          onClick={handleSave}
          disabled={isPending}
          className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors disabled:opacity-50"
        >
          <Save className="w-4 h-4" /> Save Changes
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        
        {/* Left Column: Delivery Methods */}
        <div className="space-y-6">
          <div className="bg-card border border-border rounded-xl shadow-sm p-6">
            <h3 className="font-semibold text-lg mb-6">Delivery Methods</h3>
            
            <div className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-muted rounded-md"><Bell className="w-5 h-5 text-foreground" /></div>
                  <div>
                    <p className="font-medium">In-App Push</p>
                    <p className="text-xs text-muted-foreground">Receive alerts in your browser</p>
                  </div>
                </div>
                <label className="relative inline-flex items-center cursor-pointer">
                  <input type="checkbox" className="sr-only peer" checked={formData.pushAlerts} onChange={() => handleToggle('pushAlerts')} />
                  <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
                </label>
              </div>

              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-muted rounded-md"><Mail className="w-5 h-5 text-foreground" /></div>
                  <div>
                    <p className="font-medium">Email Alerts</p>
                    <p className="text-xs text-muted-foreground">Receive daily summaries</p>
                  </div>
                </div>
                <label className="relative inline-flex items-center cursor-pointer">
                  <input type="checkbox" className="sr-only peer" checked={formData.emailAlerts} onChange={() => handleToggle('emailAlerts')} />
                  <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
                </label>
              </div>

              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-muted rounded-md"><Smartphone className="w-5 h-5 text-foreground" /></div>
                  <div>
                    <p className="font-medium">SMS Alerts</p>
                    <p className="text-xs text-muted-foreground">Critical updates only</p>
                  </div>
                </div>
                <label className="relative inline-flex items-center cursor-pointer">
                  <input type="checkbox" className="sr-only peer" checked={formData.smsAlerts} onChange={() => handleToggle('smsAlerts')} />
                  <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
                </label>
              </div>
            </div>
          </div>
        </div>

        {/* Right Column: Category Preferences */}
        <div className="md:col-span-2 bg-card border border-border rounded-xl shadow-sm p-6">
          <h3 className="font-semibold text-lg mb-6">Alert Categories</h3>
          
          <div className="space-y-4 divide-y divide-border">
            {[
              { id: 'TRIP_UPDATE', label: 'Trip Updates', desc: 'Alerts when a trip starts, reaches a waypoint, or completes.' },
              { id: 'SHIPMENT_UPDATE', label: 'Shipment Updates', desc: 'Alerts for shipment status changes and document approvals.' },
              { id: 'MATCHING', label: 'Bids & Matching', desc: 'Alerts when you receive a new bid or a match is found.' },
              { id: 'MAINTENANCE_ALERT', label: 'Fleet Maintenance', desc: 'Reminders for truck servicing and document expirations.' },
              { id: 'SYSTEM', label: 'System Announcements', desc: 'Critical platform updates and downtime notices.' },
              { id: 'MARKETING', label: 'Promotions', desc: 'Offers, discounts, and newsletter content.' },
            ].map((cat) => (
              <div key={cat.id} className="flex items-start justify-between py-4 first:pt-0 last:pb-0">
                <div className="pr-8">
                  <p className="font-medium">{cat.label}</p>
                  <p className="text-sm text-muted-foreground mt-1">{cat.desc}</p>
                </div>
                <label className="relative inline-flex items-center cursor-pointer shrink-0 mt-1">
                  <input 
                    type="checkbox" 
                    className="sr-only peer" 
                    checked={formData.categories[cat.id]} 
                    onChange={() => handleToggle(cat.id, true)} 
                    disabled={cat.id === 'SYSTEM'} // System alerts cannot be disabled
                  />
                  <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary peer-disabled:opacity-50 peer-disabled:cursor-not-allowed"></div>
                </label>
              </div>
            ))}
          </div>
        </div>

      </div>
    </PageContainer>
  );
};
