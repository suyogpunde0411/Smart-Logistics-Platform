import React, { useState } from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { NotificationCard } from '../components/NotificationCard';
import { useNotifications, useMarkAllAsRead } from '../hooks/useNotifications';
import { Search, Filter, CheckSquare, Trash2, MailOpen } from 'lucide-react';

export const NotificationCenter = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [filterCategory, setFilterCategory] = useState('ALL');
  
  const { data: notifications, isLoading } = useNotifications();
  const { mutate: markAllAsRead, isPending: markingAll } = useMarkAllAsRead();

  // Mock data fallback
  const mockNotifications = notifications || [
    { id: 1, category: 'TRIP_UPDATE', title: 'Trip Started', message: 'Truck MH 12 AB 1234 has departed for Delhi.', timestamp: 'Nov 20, 09:00 AM', isRead: false },
    { id: 2, category: 'MATCHING', title: 'New Bid Received', message: 'Express Logistics placed a bid of ₹42,000 for shipment SHP-101.', timestamp: 'Nov 20, 08:30 AM', isRead: false, actionUrl: '/matching/bids/SHP-101' },
    { id: 3, category: 'SYSTEM', title: 'Scheduled Maintenance', message: 'Platform will be offline for 2 hours tonight from 2 AM to 4 AM.', timestamp: 'Nov 19, 10:00 PM', isRead: true },
    { id: 4, category: 'SECURITY', title: 'New Login Detected', message: 'A new login was detected from Mumbai, India.', timestamp: 'Nov 18, 04:15 PM', isRead: true },
    { id: 5, category: 'SHIPMENT_UPDATE', title: 'Delivery Completed', message: 'Shipment SHP-902 was successfully delivered.', timestamp: 'Nov 17, 11:30 AM', isRead: true },
  ];

  const filteredNotifications = mockNotifications.filter(n => {
    const matchesSearch = n.title.toLowerCase().includes(searchQuery.toLowerCase()) || n.message.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesFilter = filterCategory === 'ALL' || n.category === filterCategory || (filterCategory === 'UNREAD' && !n.isRead);
    return matchesSearch && matchesFilter;
  });

  return (
    <PageContainer>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <DashboardHeader 
          title="Notification Center" 
          description="Manage your platform alerts, updates, and system announcements." 
        />
        <div className="flex gap-2">
          <button 
            onClick={() => markAllAsRead()}
            disabled={markingAll}
            className="flex items-center gap-2 px-4 py-2 border border-border rounded-lg text-sm font-medium hover:bg-muted transition-colors disabled:opacity-50"
          >
            <CheckSquare className="w-4 h-4" /> Mark All as Read
          </button>
        </div>
      </div>

      <div className="flex flex-col md:flex-row gap-4 mb-8 bg-card p-4 rounded-xl border border-border shadow-sm">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <input 
            type="text"
            placeholder="Search notifications..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 bg-background border border-input rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
        
        <div className="flex gap-2 overflow-x-auto pb-2 md:pb-0">
          {['ALL', 'UNREAD', 'TRIP_UPDATE', 'MATCHING', 'SYSTEM'].map(filter => (
            <button 
              key={filter}
              onClick={() => setFilterCategory(filter)}
              className={`px-4 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors ${filterCategory === filter ? 'bg-primary text-primary-foreground' : 'bg-background border border-border hover:bg-muted'}`}
            >
              {filter.replace('_', ' ')}
            </button>
          ))}
        </div>
      </div>

      <div className="bg-card border border-border rounded-xl shadow-sm overflow-hidden">
        {isLoading ? (
          <div className="p-12 text-center animate-pulse text-muted-foreground">Loading notifications...</div>
        ) : filteredNotifications.length === 0 ? (
          <div className="p-16 text-center flex flex-col items-center text-muted-foreground">
            <div className="w-16 h-16 bg-muted rounded-full flex items-center justify-center mb-4">
              <MailOpen className="w-8 h-8 opacity-50" />
            </div>
            <h3 className="text-lg font-medium text-foreground mb-1">No notifications found</h3>
            <p>Try adjusting your search or filters.</p>
          </div>
        ) : (
          <div className="divide-y divide-border">
            {filteredNotifications.map(notification => (
              <NotificationCard key={notification.id} notification={notification} inDrawer={true} />
            ))}
          </div>
        )}
      </div>
    </PageContainer>
  );
};
