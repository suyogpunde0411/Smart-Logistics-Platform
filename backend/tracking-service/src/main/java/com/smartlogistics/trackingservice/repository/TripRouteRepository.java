package com.smartlogistics.trackingservice.repository;

import com.smartlogistics.trackingservice.entity.TripRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRouteRepository extends JpaRepository<TripRoute, UUID> {
    Optional<TripRoute> findByTripIdAndIsDeletedFalse(UUID tripId);
}
