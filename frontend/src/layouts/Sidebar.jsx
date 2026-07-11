import React from 'react';
import { NavLink } from 'react-router-dom';
import { useSidebar } from '@/features/dashboard/hooks/useSidebar';
import { usePermissions } from '@/features/dashboard/hooks/usePermissions';
import { navigationConfig } from '@/config/navigationConfig';
import { Truck, X } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

export const Sidebar = () => {
  const { isSidebarCollapsed, isMobileSidebarOpen, setMobileSidebarOpen } = useSidebar();
  const { role } = usePermissions();

  const menuItems = navigationConfig[role] || [];

  const SidebarContent = (
    <div className="h-full flex flex-col bg-card border-r border-border shadow-sm">
      <div className="h-16 flex items-center px-4 border-b border-border justify-between">
        <div className="flex items-center gap-2 overflow-hidden">
          <div className="bg-primary p-1.5 rounded-md flex-shrink-0">
            <Truck className="w-5 h-5 text-primary-foreground" />
          </div>
          {!isSidebarCollapsed && (
            <motion.span 
              initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
              className="font-bold text-lg whitespace-nowrap text-foreground"
            >
              Smart Logistics
            </motion.span>
          )}
        </div>
        {isMobileSidebarOpen && (
          <button className="md:hidden p-1 text-muted-foreground hover:text-foreground" onClick={() => setMobileSidebarOpen(false)}>
            <X size={20} />
          </button>
        )}
      </div>

      <div className="flex-1 overflow-y-auto py-4 px-3 space-y-1">
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 px-3 py-2.5 rounded-md transition-colors ${
                  isActive
                    ? 'bg-primary/10 text-primary font-medium'
                    : 'text-muted-foreground hover:bg-muted hover:text-foreground'
                }`
              }
              title={isSidebarCollapsed ? item.name : undefined}
            >
              <Icon className="w-5 h-5 flex-shrink-0" />
              {!isSidebarCollapsed && <span className="truncate">{item.name}</span>}
            </NavLink>
          );
        })}
      </div>
    </div>
  );

  return (
    <>
      {/* Desktop Sidebar */}
      <motion.aside
        initial={false}
        animate={{ width: isSidebarCollapsed ? '4.5rem' : '16rem' }}
        className="hidden md:block h-screen flex-shrink-0 relative z-20"
      >
        {SidebarContent}
      </motion.aside>

      {/* Mobile Drawer */}
      <AnimatePresence>
        {isMobileSidebarOpen && (
          <>
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="fixed inset-0 bg-background/80 backdrop-blur-sm z-40 md:hidden"
              onClick={() => setMobileSidebarOpen(false)}
            />
            <motion.aside
              initial={{ x: '-100%' }}
              animate={{ x: 0 }}
              exit={{ x: '-100%' }}
              transition={{ type: 'spring', bounce: 0, duration: 0.3 }}
              className="fixed inset-y-0 left-0 w-64 z-50 md:hidden"
            >
              {SidebarContent}
            </motion.aside>
          </>
        )}
      </AnimatePresence>
    </>
  );
};
