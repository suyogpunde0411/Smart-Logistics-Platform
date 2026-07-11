import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { notificationCoreService } from '../services/notificationCoreService';
import toast from 'react-hot-toast';

export const useNotifications = (params) => {
  return useQuery({
    queryKey: ['notifications', params],
    queryFn: () => notificationCoreService.getNotifications(params),
    refetchInterval: 30000, // Poll for new notifications every 30 seconds
  });
};

export const useUnreadCount = () => {
  return useQuery({
    queryKey: ['unreadNotificationCount'],
    queryFn: () => notificationCoreService.getUnreadCount(),
    refetchInterval: 15000, // Poll unread count more frequently (every 15s)
  });
};

export const useMarkAsRead = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (notificationId) => notificationCoreService.markAsRead(notificationId),
    onMutate: async (notificationId) => {
      // Optimistically update the unread count
      await queryClient.cancelQueries({ queryKey: ['unreadNotificationCount'] });
      const previousCount = queryClient.getQueryData(['unreadNotificationCount']);
      if (previousCount?.count > 0) {
        queryClient.setQueryData(['unreadNotificationCount'], { count: previousCount.count - 1 });
      }
      return { previousCount };
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
    },
    onError: (err, newTodo, context) => {
      queryClient.setQueryData(['unreadNotificationCount'], context.previousCount);
      toast.error('Failed to mark notification as read');
    }
  });
};

export const useMarkAllAsRead = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => notificationCoreService.markAllAsRead(),
    onSuccess: () => {
      queryClient.setQueryData(['unreadNotificationCount'], { count: 0 });
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
      toast.success('All notifications marked as read');
    }
  });
};

export const useDeleteNotification = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (notificationId) => notificationCoreService.deleteNotification(notificationId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
      queryClient.invalidateQueries({ queryKey: ['unreadNotificationCount'] });
    }
  });
};
