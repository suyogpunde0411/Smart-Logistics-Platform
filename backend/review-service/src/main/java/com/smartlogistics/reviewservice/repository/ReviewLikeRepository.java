package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, UUID> {
    Optional<ReviewLike> findByReviewIdAndUserIdAndIsDeletedFalse(UUID reviewId, UUID userId);
    boolean existsByReviewIdAndUserIdAndIsDeletedFalse(UUID reviewId, UUID userId);
    long countByReviewIdAndIsDeletedFalse(UUID reviewId);
}
