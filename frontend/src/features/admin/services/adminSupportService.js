import apiClient from '@/api/axios';

export const adminSupportService = {
  getTickets: async (params) => {
    const response = await apiClient.get('/v1/admin/support/tickets', { params });
    return response.data;
  },
  resolveTicket: async (ticketId, resolution) => {
    const response = await apiClient.post('/v1/admin/support/tickets/' + ticketId + '/resolve', { resolution });
    return response.data;
  }
};
