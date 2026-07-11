package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, UUID> {
    Optional<FeatureFlag> findByFlagNameAndIsDeletedFalse(String flagName);
}
