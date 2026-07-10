package com.smartlogistics.trackingservice.service;

import com.smartlogistics.common.event.MatchAcceptedEvent;
import com.smartlogistics.trackingservice.dto.TripDto;
import com.smartlogistics.trackingservice.dto.TripSummaryDto;
import com.smartlogistics.trackingservice.dto.TripTimelineDto;
import com.smartlogistics.trackingservice.dto.TripRouteDto;
import com.smartlogistics.trackingservice.events.ShipmentStatusChangedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TripService {
    // Lifecycle Actions
    void createTripFromMatch(MatchAcceptedEvent event);
    TripDto.Response createTripManual(TripDto.CreateRequest request);
    TripDto.Response assignDriverAndTruck(UUID tripId, TripDto.AssignRequest request);
    TripDto.Response setTripReady(UUID tripId);
    TripDto.Response startTrip(UUID tripId);
    TripDto.Response pauseTrip(UUID tripId);
    TripDto.Response resumeTrip(UUID tripId);
    TripDto.Response completeTrip(UUID tripId);
    TripDto.Response cancelTrip(UUID tripId, String reason);
    void cancelTripForShipment(UUID shipmentId, String reason);

    // Search and Read
    TripDto.Response getTripById(UUID id);
    Page<TripDto.Response> searchTrips(
            UUID tripId, UUID shipmentId, UUID truckId, UUID driverId, UUID businessId,
            String status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Timeline & History
    List<TripTimelineDto.Response> getTripTimeline(UUID tripId);
    TripSummaryDto.Response getTripSummary(UUID tripId);
}
