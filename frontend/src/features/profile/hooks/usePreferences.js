import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { preferencesService } from '../services/preferencesService';
import toast from 'react-hot-toast';

export const useGlobalPreferences = () => {
  return useQuery({
    queryKey: ['globalPreferences'],
    queryFn: () => preferencesService.getGlobalPreferences(),
  });
};

export const useUpdateGlobalPreferences = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (prefsData) => preferencesService.updateGlobalPreferences(prefsData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['globalPreferences'] });
      toast.success('Preferences saved successfully');
    },
    onError: () => toast.error('Failed to save preferences')
  });
};
