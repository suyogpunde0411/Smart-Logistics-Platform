import { useMutation } from '@tanstack/react-query';
import { useDispatch } from 'react-redux';
import { authService } from '../services/authService';
import { logout } from '@/redux/slices/authSlice';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';

export const useLogout = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: () => authService.logout(),
    onSettled: () => {
      dispatch(logout());
      navigate('/login');
      toast.success('Logged out successfully');
    },
  });
};
