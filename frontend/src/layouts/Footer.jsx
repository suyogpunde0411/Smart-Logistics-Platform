import React from 'react';

export const Footer = () => (
  <footer className="mt-auto py-4 px-6 border-t border-border bg-card">
    <p className="text-sm text-center text-muted-foreground">
      &copy; {new Date().getFullYear()} Smart Logistics Platform. All rights reserved.
    </p>
  </footer>
);
