package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.TripDelay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripDelayRepository extends JpaRepository<TripDelay, UUID> {
    List<TripDelay> findByTripIdAndIsDeletedFalseOrderByStartTimeDesc(UUID tripId);
}
