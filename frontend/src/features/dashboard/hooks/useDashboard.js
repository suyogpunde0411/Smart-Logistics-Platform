import { useSelector } from 'react-redux';

export const useDashboard = () => {
  const { isSidebarCollapsed, isMobileSidebarOpen } = useSelector((state) => state.dashboard);
  return { isSidebarCollapsed, isMobileSidebarOpen };
};
