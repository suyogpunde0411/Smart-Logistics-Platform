import apiClient from '@/api/axios';

export const securityService = {
  changePassword: async (passwordData) => {
    const response = await apiClient.put('/v1/auth/password/change', passwordData);
    return response.data;
  },
  getActiveSessions: async () => {
    const response = await apiClient.get('/v1/auth/sessions');
    return response.data;
  },
  revokeSession: async (sessionId) => {
    const response = await apiClient.delete(`/v1/auth/sessions/${sessionId}`);
    return response.data;
  },
  deleteAccountRequest: async (reason) => {
    const response = await apiClient.post('/v1/profile/me/delete-request', { reason });
    return response.data;
  }
};
