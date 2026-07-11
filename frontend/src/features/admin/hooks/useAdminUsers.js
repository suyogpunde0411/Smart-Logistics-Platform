import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { adminUserService } from '../services/adminUserService';
import toast from 'react-hot-toast';

export const useAdminUsers = (params) => {
  return useQuery({
    queryKey: ['adminUsers', params],
    queryFn: () => adminUserService.getUsers(params),
  });
};

export const useUpdateUserStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ userId, status }) => adminUserService.updateUserStatus(userId, status),
    onSuccess: () => {
      toast.success('User status updated');
      queryClient.invalidateQueries({ queryKey: ['adminUsers'] });
    },
    onError: () => toast.error('Failed to update user status')
  });
};
