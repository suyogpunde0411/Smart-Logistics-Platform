import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { notificationPreferencesService } from '../services/notificationPreferencesService';
import toast from 'react-hot-toast';

export const useNotificationPreferences = () => {
  return useQuery({
    queryKey: ['notificationPreferences'],
    queryFn: () => notificationPreferencesService.getPreferences(),
  });
};

export const useUpdateNotificationPreferences = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (preferencesData) => notificationPreferencesService.updatePreferences(preferencesData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notificationPreferences'] });
      toast.success('Notification preferences updated successfully');
    },
    onError: () => toast.error('Failed to update preferences')
  });
};
