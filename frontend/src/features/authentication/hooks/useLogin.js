import { useMutation } from '@tanstack/react-query';
import { useDispatch } from 'react-redux';
import { authService } from '../services/authService';
import { setCredentials } from '@/redux/slices/authSlice';
import toast from 'react-hot-toast';

export const useLogin = () => {
  const dispatch = useDispatch();

  return useMutation({
    mutationFn: (credentials) => authService.login(credentials),
    onSuccess: (data) => {
      dispatch(
        setCredentials({
          user: data.user,
          accessToken: data.accessToken,
          refreshToken: data.refreshToken,
        })
      );
      toast.success('Successfully logged in');
    },
    onError: (error) => {
      const message = error.response?.data?.message || 'Failed to login';
      toast.error(message);
    },
  });
};
