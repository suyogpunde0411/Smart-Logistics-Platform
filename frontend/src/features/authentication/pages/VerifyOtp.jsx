import React from 'react';
import { Link } from 'react-router-dom';
import { CountdownTimer } from '../components/CountdownTimer';
export const VerifyOtp = () => (
  <div className="text-center space-y-4">
    <h2 className="text-xl font-semibold tracking-tight">Enter OTP</h2>
    <p className="text-sm text-muted-foreground">We sent a code to your device. <CountdownTimer initialSeconds={60} /></p>
    <Link to="/login" className="text-primary hover:underline text-sm block mt-4">Cancel</Link>
  </div>
);
