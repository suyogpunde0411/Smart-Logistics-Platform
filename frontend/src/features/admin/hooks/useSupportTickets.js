import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { adminSupportService } from '../services/adminSupportService';
import toast from 'react-hot-toast';

export const useSupportTickets = (params) => {
  return useQuery({
    queryKey: ['supportTickets', params],
    queryFn: () => adminSupportService.getTickets(params),
  });
};
