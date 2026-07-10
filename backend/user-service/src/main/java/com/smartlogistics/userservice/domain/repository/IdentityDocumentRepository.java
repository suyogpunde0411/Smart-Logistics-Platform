package com.smartlogistics.userservice.domain.repository;

import com.smartlogistics.userservice.domain.entity.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, UUID> {
    List<IdentityDocument> findByUser_Id(UUID userId);
}
