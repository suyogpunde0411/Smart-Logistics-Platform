import { useMutation } from '@tanstack/react-query';
import { useDispatch } from 'react-redux';
import { authService } from '../services/authService';
import { setCredentials, logout, setSessionExpired } from '@/redux/slices/authSlice';
import { useNavigate } from 'react-router-dom';

export const useRefreshToken = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (refreshToken) => authService.refreshToken({ refreshToken }),
    onSuccess: (data) => {
      dispatch(
        setCredentials({
          user: data.user,
          accessToken: data.accessToken,
          refreshToken: data.refreshToken,
        })
      );
    },
    onError: () => {
      dispatch(logout());
      dispatch(setSessionExpired(true));
      navigate('/session-expired');
    },
  });
};
