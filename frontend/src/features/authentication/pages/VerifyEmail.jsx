import React from 'react';
import { Link } from 'react-router-dom';
export const VerifyEmail = () => (
  <div className="text-center space-y-4">
    <h2 className="text-xl font-semibold tracking-tight">Verify Email</h2>
    <p className="text-sm text-muted-foreground">Please click the link sent to your email.</p>
    <Link to="/login" className="text-primary hover:underline text-sm block mt-4">Return to Login</Link>
  </div>
);
