import React from 'react';
import { Outlet, NavLink } from 'react-router-dom';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { User, Shield, Globe, Bell } from 'lucide-react';

export const SettingsLayout = () => {
  const navItems = [
    { name: 'Account Settings', path: '/settings/account', icon: <User className="w-4 h-4" /> },
    { name: 'Security & Login', path: '/settings/security', icon: <Shield className="w-4 h-4" /> },
    { name: 'Notifications', path: '/settings/notifications', icon: <Bell className="w-4 h-4" /> },
    { name: 'Preferences', path: '/settings/preferences', icon: <Globe className="w-4 h-4" /> },
  ];

  return (
    <PageContainer>
      <div className="mb-8">
        <DashboardHeader 
          title="Settings" 
          description="Manage your account preferences, security, and notification settings." 
        />
      </div>

      <div className="flex flex-col lg:flex-row gap-8">
        {/* Settings Sidebar */}
        <div className="lg:w-64 shrink-0">
          <nav className="flex lg:flex-col gap-2 overflow-x-auto lg:overflow-visible pb-4 lg:pb-0 hide-scrollbar">
            {navItems.map((item) => (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) => 
                  `flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm font-medium transition-colors whitespace-nowrap ${
                    isActive 
                      ? 'bg-primary text-primary-foreground shadow-sm' 
                      : 'text-muted-foreground hover:bg-muted hover:text-foreground'
                  }`
                }
              >
                {item.icon}
                {item.name}
              </NavLink>
            ))}
          </nav>
        </div>

        {/* Settings Content Area */}
        <div className="flex-1 min-w-0">
          <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
            <Outlet />
          </div>
        </div>
      </div>
    </PageContainer>
  );
};
