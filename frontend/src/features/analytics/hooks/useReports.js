import { useMutation } from '@tanstack/react-query';
import { analyticsService } from '../services/analyticsService';

export const useReports = () => useMutation({ mutationFn: analyticsService.generateReport });
