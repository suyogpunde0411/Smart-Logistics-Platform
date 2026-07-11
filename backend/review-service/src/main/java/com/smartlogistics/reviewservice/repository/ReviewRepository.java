package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    
    Optional<Review> findByIdAndIsDeletedFalse(UUID id);

    boolean existsByTripIdAndRevieweeRoleAndIsDeletedFalse(UUID tripId, String revieweeRole);

    List<Review> findByRevieweeIdAndIsDeletedFalse(UUID revieweeId);

    @Query("SELECT r FROM Review r WHERE " +
            "(:revieweeId IS NULL OR r.revieweeId = :revieweeId) AND " +
            "(:reviewerId IS NULL OR r.reviewerId = :reviewerId) AND " +
            "(:tripId IS NULL OR r.tripId = :tripId) AND " +
            "(:role IS NULL OR r.revieweeRole = :role) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(:minRating IS NULL OR r.rating.overallExperience >= :minRating) AND " +
            "(:maxRating IS NULL OR r.rating.overallExperience <= :maxRating) AND " +
            "(:startDate IS NULL OR r.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdAt <= :endDate) AND " +
            "r.isDeleted = false")
    Page<Review> searchReviews(
            @Param("revieweeId") UUID revieweeId,
            @Param("reviewerId") UUID reviewerId,
            @Param("tripId") UUID tripId,
            @Param("role") String role,
            @Param("status") String status,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
