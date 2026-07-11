import { useSelector } from 'react-redux';

export const useAuth = () => {
  const { user, accessToken, isAuthenticated, sessionExpired } = useSelector((state) => state.auth);

  return {
    user,
    accessToken,
    isAuthenticated,
    sessionExpired,
    isAdmin: user?.role === 'Admin',
    isDriver: user?.role === 'Driver',
    isBusinessOwner: user?.role === 'Business Owner',
    isFleetOwner: user?.role === 'Fleet Owner',
  };
};
