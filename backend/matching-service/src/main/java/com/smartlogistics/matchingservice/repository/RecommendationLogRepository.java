package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.RecommendationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationLogRepository extends JpaRepository<RecommendationLog, UUID> {
}
