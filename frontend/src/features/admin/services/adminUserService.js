import apiClient from '@/api/axios';

export const adminUserService = {
  getUsers: async (params) => {
    const response = await apiClient.get('/v1/admin/users', { params });
    return response.data;
  },
  updateUserStatus: async (userId, status) => {
    const response = await apiClient.patch('/v1/admin/users/' + userId + '/status', { status });
    return response.data;
  },
  getPendingVerifications: async () => {
    const response = await apiClient.get('/v1/admin/verifications/pending');
    return response.data;
  }
};
