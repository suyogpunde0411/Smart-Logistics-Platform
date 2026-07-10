package com.smartlogistics.userservice.domain.repository;

import com.smartlogistics.userservice.domain.entity.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, UUID> {
    Optional<ProfilePhoto> findByUser_Id(UUID userId);
}
