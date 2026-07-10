package com.smartlogistics.userservice.domain.repository;

import com.smartlogistics.userservice.domain.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, UUID> {
    Optional<Preference> findByUser_Id(UUID userId);
}
