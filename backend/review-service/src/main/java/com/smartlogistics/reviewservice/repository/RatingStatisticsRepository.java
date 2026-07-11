package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.RatingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RatingStatisticsRepository extends JpaRepository<RatingStatistics, UUID> {
    Optional<RatingStatistics> findByUserIdAndIsDeletedFalse(UUID userId);
}
