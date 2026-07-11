package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.BlockedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockedEntityRepository extends JpaRepository<BlockedEntity, UUID> {
    Optional<BlockedEntity> findByBlockedEntityIdAndIsDeletedFalse(UUID blockedEntityId);
}
