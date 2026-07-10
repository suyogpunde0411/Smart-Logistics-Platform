package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.MatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRequestRepository extends JpaRepository<MatchRequest, UUID> {
    Optional<MatchRequest> findByIdAndIsDeletedFalse(UUID id);
    List<MatchRequest> findByStatusAndExpiresAtBeforeAndIsDeletedFalse(String status, LocalDateTime now);
}
