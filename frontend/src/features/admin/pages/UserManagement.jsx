import React, { useState } from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { UserTable } from '../components/UserTable';
import { useAdminUsers, useUpdateUserStatus } from '../hooks/useAdminUsers';
import { Search } from 'lucide-react';

export const UserManagement = () => {
  const [roleFilter, setRoleFilter] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  
  const { data: users, isLoading } = useAdminUsers({ role: roleFilter === 'ALL' ? undefined : roleFilter });
  const { mutate: updateStatus } = useUpdateUserStatus();

  const handleStatusChange = (userId, status) => {
    updateStatus({ userId, status });
  };

  const filteredUsers = users?.filter(u => 
    (u.name?.toLowerCase() || u.firstName?.toLowerCase() || '').includes(searchQuery.toLowerCase()) ||
    u.email?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <PageContainer>
      <DashboardHeader title="User Management" description="Global directory for Drivers, Business Owners, and Fleet Owners." />
      
      <div className="flex flex-col md:flex-row justify-between items-center gap-4 mb-6">
        <div className="flex gap-2 w-full md:w-auto overflow-x-auto pb-2 md:pb-0">
          {['ALL', 'DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER'].map(tab => (
            <button 
              key={tab}
              onClick={() => setRoleFilter(tab)}
              className={"px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-colors " + (roleFilter === tab ? 'bg-primary text-primary-foreground' : 'bg-muted text-muted-foreground hover:bg-muted/80')}
            >
              {tab.replace('_', ' ')}
            </button>
          ))}
        </div>
        
        <div className="relative w-full md:w-64">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <input 
            type="text"
            placeholder="Search users..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 bg-card border border-border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
      </div>

      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading users...</div>
      ) : (
        <UserTable users={filteredUsers} onStatusChange={handleStatusChange} />
      )}
    </PageContainer>
  );
};
