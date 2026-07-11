import { describe, it, expect } from 'vitest';
import { authService } from '../services/authService';

describe('authService', () => {
  it('should call login endpoint', async () => {
    const res = await authService.login({ email: 'test@example.com', password: 'password' });
    expect(res.accessToken).toBe('mock-access-token');
  });
});
