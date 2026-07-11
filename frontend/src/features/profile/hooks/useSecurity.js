import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { securityService } from '../services/securityService';
import toast from 'react-hot-toast';

export const useChangePassword = () => {
  return useMutation({
    mutationFn: (passwordData) => securityService.changePassword(passwordData),
    onSuccess: () => toast.success('Password changed successfully'),
    onError: () => toast.error('Failed to change password')
  });
};

export const useActiveSessions = () => {
  return useQuery({
    queryKey: ['activeSessions'],
    queryFn: () => securityService.getActiveSessions(),
  });
};

export const useRevokeSession = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (sessionId) => securityService.revokeSession(sessionId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['activeSessions'] });
      toast.success('Session revoked');
    },
    onError: () => toast.error('Failed to revoke session')
  });
};

export const useDeleteAccountRequest = () => {
  return useMutation({
    mutationFn: (reason) => securityService.deleteAccountRequest(reason),
    onSuccess: () => toast.success('Account deletion requested. Admin will contact you.'),
    onError: () => toast.error('Failed to submit request')
  });
};
