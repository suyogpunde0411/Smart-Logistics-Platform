import React from 'react';
import { DashboardHeader } from '@/features/dashboard/components/DashboardHeader';
import { PageContainer } from '@/features/dashboard/components/PageContainer';
import { ReviewModerationTable } from '../components/ReviewModerationTable';
import { useReportedReviews, useModerateReview } from '../hooks/useReportedReviews';

export const ReviewModeration = () => {
  const { data: reviews, isLoading } = useReportedReviews();
  const { mutate: moderateReview } = useModerateReview();

  const handleModerate = (reviewId, action) => {
    moderateReview({ reviewId, action });
  };

  // Mock data for display if backend is empty
  const mockReviews = reviews?.length ? reviews : [
    { id: 1, rating: 1, comment: 'Driver was extremely rude and drove dangerously.', reportReason: 'Harassment', authorName: 'John Doe' },
    { id: 2, rating: 5, comment: 'Spam link: http://buy-cheap-trucks.com', reportReason: 'Spam/Advertising', authorName: 'SpamBot99' }
  ];

  return (
    <PageContainer>
      <DashboardHeader title="Review Moderation" description="Review and action user-reported feedback." />
      {isLoading ? (
        <div className="p-8 text-center animate-pulse text-muted-foreground">Loading reported reviews...</div>
      ) : (
        <ReviewModerationTable reviews={mockReviews} onModerate={handleModerate} />
      )}
    </PageContainer>
  );
};
