import apiClient from '@/api/axios';

export const fleetMaintenanceService = {
  getMaintenanceLogs: async (params) => {
    const response = await apiClient.get('/v1/fleet/maintenance', { params });
    return response.data;
  },
  scheduleMaintenance: async (data) => {
    const response = await apiClient.post('/v1/fleet/maintenance', data);
    return response.data;
  }
};
