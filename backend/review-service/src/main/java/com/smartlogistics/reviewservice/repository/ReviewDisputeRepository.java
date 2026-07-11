package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.ReviewDispute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewDisputeRepository extends JpaRepository<ReviewDispute, UUID> {
    Optional<ReviewDispute> findByIdAndIsDeletedFalse(UUID id);
    List<ReviewDispute> findByReviewIdAndIsDeletedFalse(UUID reviewId);
}
