package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, UUID> {
    Optional<ReviewReport> findByIdAndIsDeletedFalse(UUID id);
    List<ReviewReport> findByReviewIdAndIsDeletedFalse(UUID reviewId);
}
