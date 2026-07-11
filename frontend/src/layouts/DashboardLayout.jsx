import React from 'react';
import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Topbar } from './Topbar';
import { Footer } from './Footer';
import { useBreadcrumb } from '@/features/dashboard/hooks/useBreadcrumb';
import { ChevronRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import { PageLoader } from '@/components/common/PageLoader';

export const DashboardLayout = () => {
  const breadcrumbs = useBreadcrumb();

  return (
    <div className="flex h-screen overflow-hidden bg-background">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0">
        <Topbar />
        
        <main className="flex-1 overflow-y-auto bg-muted/20">
          <div className="p-4 md:p-6 lg:p-8 max-w-7xl mx-auto w-full">
            {/* Breadcrumb */}
            {breadcrumbs.length > 1 && (
              <nav className="flex items-center text-sm text-muted-foreground mb-6">
                {breadcrumbs.map((crumb, idx) => {
                  const isLast = idx === breadcrumbs.length - 1;
                  return (
                    <div key={crumb.to} className="flex items-center">
                      {isLast ? (
                        <span className="font-medium text-foreground">{crumb.name}</span>
                      ) : (
                        <>
                          <Link to={crumb.to} className="hover:text-foreground transition-colors">
                            {crumb.name}
                          </Link>
                          <ChevronRight className="w-4 h-4 mx-1" />
                        </>
                      )}
                    </div>
                  );
                })}
              </nav>
            )}
            
            {/* Page Content */}
            <React.Suspense fallback={<PageLoader />}>
              <Outlet />
            </React.Suspense>
          </div>
        </main>
        
        <Footer />
      </div>
    </div>
  );
};
