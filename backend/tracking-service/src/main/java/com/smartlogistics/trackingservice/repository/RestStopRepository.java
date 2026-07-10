package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.RestStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestStopRepository extends JpaRepository<RestStop, UUID> {
    List<RestStop> findByTripIdAndIsDeletedFalseOrderByStartTimeDesc(UUID tripId);
}
