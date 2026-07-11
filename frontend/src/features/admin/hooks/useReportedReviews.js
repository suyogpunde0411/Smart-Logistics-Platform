import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { adminReviewService } from '../services/adminReviewService';
import toast from 'react-hot-toast';

export const useReportedReviews = () => {
  return useQuery({
    queryKey: ['reportedReviews'],
    queryFn: adminReviewService.getReportedReviews,
  });
};

export const useModerateReview = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ reviewId, action }) => adminReviewService.moderateReview(reviewId, action),
    onSuccess: () => {
      toast.success('Review moderated');
      queryClient.invalidateQueries({ queryKey: ['reportedReviews'] });
    },
    onError: () => toast.error('Failed to moderate review')
  });
};
