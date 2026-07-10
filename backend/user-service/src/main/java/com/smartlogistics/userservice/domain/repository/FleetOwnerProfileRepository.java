package com.smartlogistics.userservice.domain.repository;

import com.smartlogistics.userservice.domain.entity.FleetOwnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FleetOwnerProfileRepository extends JpaRepository<FleetOwnerProfile, UUID> {
    Optional<FleetOwnerProfile> findByUser_Id(UUID userId);
}
