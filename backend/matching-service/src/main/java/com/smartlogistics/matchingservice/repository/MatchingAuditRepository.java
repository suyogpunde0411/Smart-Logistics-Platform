package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.MatchingAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchingAuditRepository extends JpaRepository<MatchingAudit, UUID> {
}
