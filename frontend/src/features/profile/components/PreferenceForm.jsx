import React, { useState, useEffect } from 'react';
import { Globe, Clock, MonitorSmartphone, Eye, VolumeX, Save } from 'lucide-react';
import { useGlobalPreferences, useUpdateGlobalPreferences } from '../hooks/usePreferences';
import { useDispatch, useSelector } from 'react-redux';
import { toggleTheme } from '@/redux/slices/themeSlice';

export const PreferenceForm = () => {
  const { data: preferences, isLoading } = useGlobalPreferences();
  const { mutate: updatePreferences, isPending } = useUpdateGlobalPreferences();
  const dispatch = useDispatch();
  const currentTheme = useSelector(state => state.theme.theme);

  const [formData, setFormData] = useState({
    language: 'en',
    timezone: 'Asia/Kolkata',
    dateFormat: 'DD/MM/YYYY',
    distanceUnit: 'km',
    currency: 'INR',
    highContrast: false,
    reducedMotion: false
  });

  useEffect(() => {
    if (preferences) setFormData(preferences);
  }, [preferences]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSave = () => {
    updatePreferences(formData);
  };

  if (isLoading) return <div className="p-8 text-center animate-pulse text-muted-foreground">Loading preferences...</div>;

  return (
    <div className="space-y-6">
      
      {/* Localization Settings */}
      <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
        <div className="p-6 border-b border-border bg-muted/10">
          <h3 className="font-semibold text-lg flex items-center gap-2"><Globe className="w-5 h-5 text-primary" /> Localization</h3>
          <p className="text-sm text-muted-foreground mt-1">Configure your region, language, and measurement units.</p>
        </div>
        <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-sm font-medium mb-1">Language</label>
            <select name="language" value={formData.language} onChange={handleChange} className="w-full px-3 py-2 bg-background border border-input rounded-lg text-sm focus:ring-2 focus:ring-primary">
              <option value="en">English (US)</option>
              <option value="en-IN">English (India)</option>
              <option value="hi">Hindi (हिंदी)</option>
              <option value="mr">Marathi (मराठी)</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1 flex items-center gap-1"><Clock className="w-4 h-4" /> Timezone</label>
            <select name="timezone" value={formData.timezone} onChange={handleChange} className="w-full px-3 py-2 bg-background border border-input rounded-lg text-sm focus:ring-2 focus:ring-primary">
              <option value="Asia/Kolkata">India Standard Time (IST)</option>
              <option value="UTC">Coordinated Universal Time (UTC)</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Date Format</label>
            <select name="dateFormat" value={formData.dateFormat} onChange={handleChange} className="w-full px-3 py-2 bg-background border border-input rounded-lg text-sm focus:ring-2 focus:ring-primary">
              <option value="DD/MM/YYYY">DD/MM/YYYY</option>
              <option value="MM/DD/YYYY">MM/DD/YYYY</option>
              <option value="YYYY-MM-DD">YYYY-MM-DD</option>
            </select>
          </div>
          <div className="flex gap-4">
            <div className="flex-1">
              <label className="block text-sm font-medium mb-1">Distance Unit</label>
              <select name="distanceUnit" value={formData.distanceUnit} onChange={handleChange} className="w-full px-3 py-2 bg-background border border-input rounded-lg text-sm focus:ring-2 focus:ring-primary">
                <option value="km">Kilometers (km)</option>
                <option value="mi">Miles (mi)</option>
              </select>
            </div>
            <div className="flex-1">
              <label className="block text-sm font-medium mb-1">Currency</label>
              <select name="currency" value={formData.currency} onChange={handleChange} className="w-full px-3 py-2 bg-background border border-input rounded-lg text-sm focus:ring-2 focus:ring-primary">
                <option value="INR">INR (₹)</option>
                <option value="USD">USD ($)</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      {/* Appearance & Accessibility */}
      <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
        <div className="p-6 border-b border-border bg-muted/10">
          <h3 className="font-semibold text-lg flex items-center gap-2"><Eye className="w-5 h-5 text-primary" /> Appearance & Accessibility</h3>
          <p className="text-sm text-muted-foreground mt-1">Customize the platform interface to suit your visual needs.</p>
        </div>
        <div className="p-6 space-y-6">
          
          {/* Theme Toggle */}
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-muted rounded-md"><MonitorSmartphone className="w-5 h-5 text-foreground" /></div>
              <div>
                <p className="font-medium">Dark Mode</p>
                <p className="text-xs text-muted-foreground">Toggle the global application theme</p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" className="sr-only peer" checked={currentTheme === 'dark'} onChange={() => dispatch(toggleTheme())} />
              <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
            </label>
          </div>

          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-muted rounded-md"><Eye className="w-5 h-5 text-foreground" /></div>
              <div>
                <p className="font-medium">High Contrast Mode</p>
                <p className="text-xs text-muted-foreground">Increase color contrast for better readability</p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" name="highContrast" className="sr-only peer" checked={formData.highContrast} onChange={handleChange} />
              <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
            </label>
          </div>

          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-muted rounded-md"><VolumeX className="w-5 h-5 text-foreground" /></div>
              <div>
                <p className="font-medium">Reduced Motion</p>
                <p className="text-xs text-muted-foreground">Minimize UI animations and transitions</p>
              </div>
            </div>
            <label className="relative inline-flex items-center cursor-pointer">
              <input type="checkbox" name="reducedMotion" className="sr-only peer" checked={formData.reducedMotion} onChange={handleChange} />
              <div className="w-11 h-6 bg-muted peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
            </label>
          </div>

        </div>
      </div>

      <div className="flex justify-end">
        <button 
          onClick={handleSave}
          disabled={isPending}
          className="flex items-center gap-2 bg-primary text-primary-foreground px-6 py-2 rounded-lg font-medium hover:bg-primary/90 transition-colors disabled:opacity-50"
        >
          <Save className="w-4 h-4" /> Save Preferences
        </button>
      </div>
    </div>
  );
};
