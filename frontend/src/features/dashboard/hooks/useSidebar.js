import { useDispatch, useSelector } from 'react-redux';
import {
  toggleSidebar,
  setSidebarCollapsed,
  toggleMobileSidebar,
  setMobileSidebarOpen,
} from '@/redux/slices/dashboardSlice';

export const useSidebar = () => {
  const dispatch = useDispatch();
  const { isSidebarCollapsed, isMobileSidebarOpen } = useSelector((state) => state.dashboard);

  return {
    isSidebarCollapsed,
    isMobileSidebarOpen,
    toggleSidebar: () => dispatch(toggleSidebar()),
    setSidebarCollapsed: (val) => dispatch(setSidebarCollapsed(val)),
    toggleMobileSidebar: () => dispatch(toggleMobileSidebar()),
    setMobileSidebarOpen: (val) => dispatch(setMobileSidebarOpen(val)),
  };
};
