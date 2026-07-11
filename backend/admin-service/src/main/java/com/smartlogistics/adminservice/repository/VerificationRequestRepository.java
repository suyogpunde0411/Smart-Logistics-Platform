package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.VerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, UUID> {
    Optional<VerificationRequest> findByEntityIdAndIsDeletedFalse(UUID entityId);
    List<VerificationRequest> findByStatusAndIsDeletedFalse(String status);
    List<VerificationRequest> findByStatus(String status);
    List<VerificationRequest> findByEntityTypeAndIsDeletedFalse(String entityType);
}
