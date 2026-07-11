import apiClient from '@/api/axios';

export const truckInsuranceService = {
  getInsuranceDetails: async (truckId) => {
    const response = await apiClient.get(`/v1/trucks/${truckId}/insurance`);
    return response.data;
  },
  updateInsurance: async (truckId, insuranceData) => {
    const response = await apiClient.put(`/v1/trucks/${truckId}/insurance`, insuranceData);
    return response.data;
  }
};
