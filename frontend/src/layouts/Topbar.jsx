import React from 'react';
import { useSidebar } from '@/features/dashboard/hooks/useSidebar';
import { useLogout } from '@/features/authentication/hooks/useLogout';
import { Menu, Bell, Search, Sun, Moon, LogOut, User, Settings } from 'lucide-react';
import { useSelector, useDispatch } from 'react-redux';
import { toggleTheme } from '@/redux/slices/themeSlice';
import { NotificationBell } from '@/features/notifications/components/NotificationBell';

export const Topbar = () => {
  const { toggleSidebar, setMobileSidebarOpen } = useSidebar();
  const { user } = useSelector((state) => state.auth);
  const { theme } = useSelector((state) => state.theme);
  const dispatch = useDispatch();
  const { mutate: logout } = useLogout();

  return (
    <header className="h-16 flex items-center justify-between px-4 bg-card border-b border-border shadow-sm sticky top-0 z-30">
      <div className="flex items-center gap-4">
        {/* Mobile menu toggle */}
        <button
          className="md:hidden p-2 text-muted-foreground hover:bg-muted rounded-md"
          onClick={() => setMobileSidebarOpen(true)}
        >
          <Menu size={20} />
        </button>
        {/* Desktop sidebar toggle */}
        <button
          className="hidden md:block p-2 text-muted-foreground hover:bg-muted rounded-md"
          onClick={toggleSidebar}
        >
          <Menu size={20} />
        </button>

        {/* Global Search Placeholder */}
        <div className="hidden sm:flex items-center relative w-64">
          <Search className="w-4 h-4 absolute left-3 text-muted-foreground" />
          <input
            type="text"
            placeholder="Search..."
            className="w-full h-9 pl-9 pr-4 rounded-md border border-input bg-background text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
          />
        </div>
      </div>

      <div className="flex items-center gap-3">
        <button
          onClick={() => dispatch(toggleTheme())}
          className="p-2 text-muted-foreground hover:bg-muted rounded-full"
        >
          {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
        </button>
        
        <NotificationBell />


        <div className="flex items-center gap-2 pl-2 border-l border-border ml-1">
          <div className="hidden md:flex flex-col items-end">
            <span className="text-sm font-medium text-foreground">{user?.email?.split('@')[0] || 'User'}</span>
            <span className="text-xs text-muted-foreground">{user?.role}</span>
          </div>
          <div className="w-8 h-8 rounded-full bg-primary/20 flex items-center justify-center text-primary relative group cursor-pointer">
            <User size={16} />
            <div className="absolute top-full right-0 mt-2 w-48 bg-card border border-border shadow-md rounded-md py-1 hidden group-hover:block">
              <button
                onClick={() => window.location.href = '/profile'}
                className="w-full text-left px-4 py-2 text-sm text-foreground hover:bg-muted flex items-center gap-2"
              >
                <User size={16} /> Profile
              </button>
              <button
                onClick={() => window.location.href = '/settings/account'}
                className="w-full text-left px-4 py-2 text-sm text-foreground hover:bg-muted flex items-center gap-2 mb-1 border-b border-border pb-2"
              >
                <Settings size={16} /> Settings
              </button>
              <button
                onClick={() => logout()}
                className="w-full text-left px-4 py-2 text-sm text-destructive hover:bg-muted flex items-center gap-2 mt-1"
              >
                <LogOut size={16} /> Logout
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};
