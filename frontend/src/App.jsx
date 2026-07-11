import React from 'react';
import { RouterProvider } from 'react-router-dom';
import { router } from '@/routes';
import { ThemeProvider } from '@/providers/ThemeProvider';

function App() {
  return (
    <ThemeProvider>
      <div className="min-h-screen bg-background text-foreground font-sans antialiased">
        <RouterProvider router={router} />
      </div>
    </ThemeProvider>
  );
}

export default App;
