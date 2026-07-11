import apiClient from '@/api/axios';

export const truckMaintenanceService = {
  getMaintenanceLogs: async (truckId) => {
    const response = await apiClient.get(`/v1/trucks/${truckId}/maintenance`);
    return response.data;
  },
  logMaintenance: async (truckId, logData) => {
    const response = await apiClient.post(`/v1/trucks/${truckId}/maintenance`, logData);
    return response.data;
  }
};
