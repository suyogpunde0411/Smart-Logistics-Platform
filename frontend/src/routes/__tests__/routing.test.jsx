import React from 'react';
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { RoleProtectedRoute } from '../RoleProtectedRoute';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import authReducer from '@/redux/slices/authSlice';

// Helper to configure a mock store with specified auth state
const createMockStore = (initialAuthState) => {
  return configureStore({
    reducer: {
      auth: authReducer,
    },
    preloadedState: {
      auth: initialAuthState,
    },
  });
};

describe('RoleProtectedRoute Component', () => {
  it('redirects or renders unauthorized message if role is not allowed', () => {
    const store = createMockStore({
      user: { id: 1, role: 'Driver' },
      isAuthenticated: true
    });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/admin-only']}>
          <Routes>
            <Route element={<RoleProtectedRoute allowedRoles={['Admin']} />}>
              <Route path="/admin-only" element={<div>Admin Page</div>} />
            </Route>
            <Route path="/unauthorized" element={<div>403 Unauthorized</div>} />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    // Should redirect to /unauthorized because Driver is not Admin
    expect(screen.getByText('403 Unauthorized')).toBeInTheDocument();
    expect(screen.queryByText('Admin Page')).not.toBeInTheDocument();
  });

  it('renders children routes if role matches allowed list', () => {
    const store = createMockStore({
      user: { id: 1, role: 'Admin' },
      isAuthenticated: true
    });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/admin-only']}>
          <Routes>
            <Route element={<RoleProtectedRoute allowedRoles={['Admin']} />}>
              <Route path="/admin-only" element={<div>Admin Page</div>} />
            </Route>
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Admin Page')).toBeInTheDocument();
  });
});
