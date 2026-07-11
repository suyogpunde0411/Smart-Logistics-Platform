import apiClient from '@/api/axios';

const DASHBOARD_URL = '/v1/analytics/dashboard';
const REPORTS_URL = '/v1/analytics/reports';

const cleanParams = (params = {}) => Object.fromEntries(
  Object.entries(params).filter(([, value]) => value !== undefined && value !== null && value !== '')
);

export const analyticsService = {
  getDashboard: async () => (await apiClient.get(`${DASHBOARD_URL}/summary`)).data,
  getKpis: async () => (await apiClient.get(`${DASHBOARD_URL}/kpis`)).data,
  getTopRoutes: async () => (await apiClient.get(`${DASHBOARD_URL}/top-routes`)).data,
  getTrips: async (filters) => (await apiClient.get(`${DASHBOARD_URL}/trips`, { params: cleanParams(filters) })).data,
  generateReport: async (request) => (await apiClient.post(`${REPORTS_URL}/generate`, request)).data,
  exportReport: async (request) => (await apiClient.post(`${REPORTS_URL}/export`, request, { responseType: 'blob' })).data,
};
