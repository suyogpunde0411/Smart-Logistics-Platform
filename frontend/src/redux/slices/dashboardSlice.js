import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  isSidebarCollapsed: false,
  isMobileSidebarOpen: false,
};

const dashboardSlice = createSlice({
  name: 'dashboard',
  initialState,
  reducers: {
    toggleSidebar: (state) => {
      state.isSidebarCollapsed = !state.isSidebarCollapsed;
    },
    setSidebarCollapsed: (state, action) => {
      state.isSidebarCollapsed = action.payload;
    },
    toggleMobileSidebar: (state) => {
      state.isMobileSidebarOpen = !state.isMobileSidebarOpen;
    },
    setMobileSidebarOpen: (state, action) => {
      state.isMobileSidebarOpen = action.payload;
    },
  },
});

export const {
  toggleSidebar,
  setSidebarCollapsed,
  toggleMobileSidebar,
  setMobileSidebarOpen,
} = dashboardSlice.actions;

export default dashboardSlice.reducer;
