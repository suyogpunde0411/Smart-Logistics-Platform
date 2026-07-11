package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, UUID> {
    List<DocumentVerification> findByVerificationRequestIdAndIsDeletedFalse(UUID verificationRequestId);
}
