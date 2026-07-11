import apiClient from '@/api/axios';

export const notificationCoreService = {
  getNotifications: async (params) => {
    const response = await apiClient.get('/v1/notifications', { params });
    return response.data;
  },
  getUnreadCount: async () => {
    const response = await apiClient.get('/v1/notifications/unread-count');
    return response.data;
  },
  markAsRead: async (notificationId) => {
    const response = await apiClient.patch(`/v1/notifications/${notificationId}/read`);
    return response.data;
  },
  markAllAsRead: async () => {
    const response = await apiClient.patch(`/v1/notifications/read-all`);
    return response.data;
  },
  deleteNotification: async (notificationId) => {
    const response = await apiClient.delete(`/v1/notifications/${notificationId}`);
    return response.data;
  }
};
