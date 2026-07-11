package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByIdAndIsDeletedFalse(UUID id);
}
