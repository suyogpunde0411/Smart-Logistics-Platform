package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.TrustScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrustScoreRepository extends JpaRepository<TrustScore, UUID> {
    Optional<TrustScore> findByUserIdAndIsDeletedFalse(UUID userId);
}
