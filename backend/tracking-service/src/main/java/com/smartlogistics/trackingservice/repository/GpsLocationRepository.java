package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.GpsLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GpsLocationRepository extends JpaRepository<GpsLocation, UUID> {
    Page<GpsLocation> findByTripIdAndIsDeletedFalseOrderByTimestampDesc(UUID tripId, Pageable pageable);
    List<GpsLocation> findByTripIdAndIsDeletedFalseOrderByTimestampAsc(UUID tripId);
    void deleteByTripIdAndTimestampBefore(UUID tripId, LocalDateTime cutoff);
}
