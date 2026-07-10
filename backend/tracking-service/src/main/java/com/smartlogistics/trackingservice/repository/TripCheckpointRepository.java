package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.TripCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripCheckpointRepository extends JpaRepository<TripCheckpoint, UUID> {
    List<TripCheckpoint> findByTripIdAndIsDeletedFalseOrderBySequenceIndexAsc(UUID tripId);
}
