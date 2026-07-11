import apiClient from '@/api/axios';

export const adminReviewService = {
  getReportedReviews: async () => {
    const response = await apiClient.get('/v1/admin/reviews/reported');
    return response.data;
  },
  moderateReview: async (reviewId, action) => {
    const response = await apiClient.post('/v1/admin/reviews/' + reviewId + '/moderate', { action });
    return response.data;
  }
};
