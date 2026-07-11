import authReducer, { setCredentials, logout } from '@/redux/slices/authSlice';
import { describe, it, expect } from 'vitest';

describe('authSlice', () => {
  it('should handle setCredentials', () => {
    const initialState = { user: null, accessToken: null, refreshToken: null, isAuthenticated: false };
    const action = setCredentials({ user: { id: 1 }, accessToken: '123', refreshToken: '456' });
    const state = authReducer(initialState, action);
    expect(state.isAuthenticated).toBe(true);
    expect(state.accessToken).toBe('123');
  });
});
