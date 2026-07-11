import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { adminSystemService } from '../services/adminSystemService';
import toast from 'react-hot-toast';

export const useSystemSettings = () => {
  return useQuery({
    queryKey: ['systemSettings'],
    queryFn: adminSystemService.getSettings,
  });
};

export const useUpdateSetting = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ key, value }) => adminSystemService.updateSetting(key, value),
    onSuccess: () => {
      toast.success('Settings updated');
      queryClient.invalidateQueries({ queryKey: ['systemSettings'] });
    },
    onError: () => toast.error('Failed to update settings')
  });
};
