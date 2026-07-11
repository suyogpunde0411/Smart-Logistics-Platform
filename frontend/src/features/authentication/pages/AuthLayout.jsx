import React from 'react';
import { Outlet, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Truck } from 'lucide-react';

export const AuthLayout = () => {
  const { isAuthenticated } = useSelector((state) => state.auth);

  if (isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className="min-h-screen bg-muted/30 flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-card rounded-xl shadow-lg border border-border p-6 sm:p-8">
        <div className="flex flex-col items-center justify-center mb-8">
          <div className="bg-primary p-3 rounded-full mb-4">
            <Truck className="w-8 h-8 text-primary-foreground" />
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-foreground text-center">
            Smart Logistics
          </h1>
          <p className="text-sm text-muted-foreground text-center mt-1">
            Optimization Platform
          </p>
        </div>
        <Outlet />
      </div>
    </div>
  );
};
