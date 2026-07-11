import React from 'react';
import { Link } from 'react-router-dom';

export const SessionExpired = () => {
  return (
    <div className="text-center space-y-4">
      <h2 className="text-xl font-semibold tracking-tight text-destructive">Session Expired</h2>
      <p className="text-sm text-muted-foreground">Your session has expired or is invalid. Please log in again to continue.</p>
      <Link to="/login" className="inline-flex items-center justify-center rounded-md text-sm font-medium bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 mt-4">
        Go to Login
      </Link>
    </div>
  );
};
