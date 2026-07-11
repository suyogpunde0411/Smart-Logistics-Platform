import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { profileService } from '../services/profileService';
import toast from 'react-hot-toast';

export const useProfile = () => {
  return useQuery({
    queryKey: ['userProfile'],
    queryFn: () => profileService.getProfile(),
  });
};

export const useUpdateProfile = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (profileData) => profileService.updateProfile(profileData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['userProfile'] });
      toast.success('Profile updated successfully');
    },
    onError: () => toast.error('Failed to update profile')
  });
};

export const useAvatarUpload = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (file) => profileService.uploadAvatar(file),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['userProfile'] });
      toast.success('Avatar uploaded successfully');
    },
    onError: () => toast.error('Failed to upload avatar')
  });
};

export const useDeleteAvatar = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => profileService.deleteAvatar(),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['userProfile'] });
      toast.success('Avatar removed');
    },
    onError: () => toast.error('Failed to remove avatar')
  });
};
