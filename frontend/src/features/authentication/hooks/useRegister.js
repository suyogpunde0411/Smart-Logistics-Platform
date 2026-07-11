import { useMutation } from '@tanstack/react-query';
import { authService } from '../services/authService';
import toast from 'react-hot-toast';

export const useRegister = () => {
  return useMutation({
    mutationFn: (data) => authService.register(data),
    onSuccess: () => {
      toast.success('Registration successful. Please verify your email.');
    },
    onError: (error) => {
      const message = error.response?.data?.message || 'Registration failed';
      toast.error(message);
    },
  });
};
