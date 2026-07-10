package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.TripSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripSummaryRepository extends JpaRepository<TripSummary, UUID> {
    Optional<TripSummary> findByTripIdAndIsDeletedFalse(UUID tripId);
}
