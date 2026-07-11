import { http, HttpResponse } from 'msw';

export const handlers = [
  http.post('/api/v1/auth/login', async ({ request }) => {
    const { email } = await request.json();
    if (email === 'test@example.com') {
      return HttpResponse.json({
        user: { id: 1, email: 'test@example.com', role: 'Driver' },
        accessToken: 'mock-access-token',
        refreshToken: 'mock-refresh-token'
      });
    }
    return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 });
  })
];
