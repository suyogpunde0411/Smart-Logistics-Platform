package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, UUID> {
    Optional<SystemConfiguration> findByConfigKeyAndIsDeletedFalse(String configKey);
}
