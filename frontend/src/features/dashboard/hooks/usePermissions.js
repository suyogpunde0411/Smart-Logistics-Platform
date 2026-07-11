import { useSelector } from 'react-redux';

export const usePermissions = () => {
  const { user } = useSelector((state) => state.auth);

  const hasRole = (role) => {
    if (Array.isArray(role)) {
      return role.includes(user?.role);
    }
    return user?.role === role;
  };

  const isSuperAdmin = hasRole('Super Admin');

  return {
    user,
    role: user?.role,
    hasRole,
    isSuperAdmin,
  };
};
