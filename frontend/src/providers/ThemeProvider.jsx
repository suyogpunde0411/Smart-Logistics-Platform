import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';

export const ThemeProvider = ({ children }) => {
  const theme = useSelector((state) => state.theme.theme);

  useEffect(() => {
    const root = window.document.documentElement;
    root.classList.remove('light', 'dark');

    if (theme === 'system') {
      const systemTheme = window.matchMedia('(prefers-color-scheme: dark)')
        .matches
        ? 'dark'
        : 'light';
      root.classList.add(systemTheme);
      return;
    }

    root.classList.add(theme);
  }, [theme]);

  return <>{children}</>;
};
