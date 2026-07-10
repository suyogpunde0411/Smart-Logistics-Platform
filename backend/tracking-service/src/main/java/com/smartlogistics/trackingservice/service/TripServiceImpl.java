package com.smartlogistics.trackingservice.service;

import com.smartlogistics.common.event.MatchAcceptedEvent;
import com.smartlogistics.trackingservice.client.ShipmentClient;
import com.smartlogistics.trackingservice.client.TruckClient;
import com.smartlogistics.trackingservice.client.UserClient;
import com.smartlogistics.trackingservice.dto.TripDto;
import com.smartlogistics.trackingservice.dto.TripSummaryDto;
import com.smartlogistics.trackingservice.dto.TripTimelineDto;
import com.smartlogistics.trackingservice.dto.TripRouteDto;
import com.smartlogistics.trackingservice.dto.CheckpointDto;
import com.smartlogistics.trackingservice.entity.*;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;
import com.smartlogistics.trackingservice.exception.TripNotFoundException;
import com.smartlogistics.trackingservice.events.TrackingKafkaProducer;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.*;
import com.smartlogistics.trackingservice.service.state.TripState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripRouteRepository routeRepository;
    private final TripCheckpointRepository checkpointRepository;
    private final TripTimelineRepository timelineRepository;
    private final TripSummaryRepository summaryRepository;

    private final ShipmentClient shipmentClient;
    private final TruckClient truckClient;
    private final UserClient userClient;

    private final TrackingKafkaProducer kafkaProducer;
    private final TripMapper tripMapper;

    @Override
    @Transactional
    public void createTripFromMatch(MatchAcceptedEvent event) {
        // Prevent duplicates
        boolean exists = tripRepository.existsByShipmentIdAndStatusNotAndIsDeletedFalse(
                event.shipmentId(), "CANCELLED");
        if (exists) {
            log.warn("Trip already exists and is active for shipment ID: {}", event.shipmentId());
            return;
        }

        // Fetch details from ShipmentClient (internal)
        ShipmentClient.InternalShipmentResponse shipment = null;
        try {
            shipment = shipmentClient.getShipment(event.shipmentId());
        } catch (Exception e) {
            log.error("Failed to retrieve shipment details via Feign for ID: {}. Error: {}", event.shipmentId(), e.getMessage());
        }

        UUID businessId = shipment != null ? shipment.businessId() : UUID.randomUUID();

        // Create Trip
        Trip trip = Trip.builder()
                .shipmentId(event.shipmentId())
                .truckId(event.truckId())
                .driverId(event.driverId())
                .businessId(businessId)
                .status("ASSIGNED")
                .totalDistanceTravelledKm(0.0)
                .remainingDistanceKm(500.0) // default fallback
                .averageSpeedKmh(50.0)
                .build();

        // Setup Planned Route
        String startAddr = shipment != null ? shipment.origin() : "Origin Address";
        String endAddr = shipment != null ? shipment.destination() : "Destination Address";

        // Fallback coordinates (Mumbai to Delhi)
        Double startLat = 19.0760;
        Double startLng = 72.8777;
        Double endLat = 28.6139;
        Double endLng = 77.2090;

        // Try getting actual coordinates using detailed getShipment endpoint (if it succeeds)
        try {
            ShipmentClient.DetailedShipmentResponse detailedShipment = shipmentClient.getShipmentDetailed(event.shipmentId());
            if (detailedShipment != null) {
                if (detailedShipment.originLatitude() != null) startLat = detailedShipment.originLatitude();
                if (detailedShipment.originLongitude() != null) startLng = detailedShipment.originLongitude();
                if (detailedShipment.destinationLatitude() != null) endLat = detailedShipment.destinationLatitude();
                if (detailedShipment.destinationLongitude() != null) endLng = detailedShipment.destinationLongitude();
                if (detailedShipment.originAddress() != null) startAddr = detailedShipment.originAddress();
                if (detailedShipment.destinationAddress() != null) endAddr = detailedShipment.destinationAddress();
            }
        } catch (Exception e) {
            log.warn("Could not retrieve detailed shipment coordinates. Using fallbacks. Error: {}", e.getMessage());
        }

        TripRoute route = TripRoute.builder()
                .trip(trip)
                .startAddress(startAddr)
                .startLatitude(startLat)
                .startLongitude(startLng)
                .endAddress(endAddr)
                .endLatitude(endLat)
                .endLongitude(endLng)
                .plannedDistanceKm(500.0)
                .plannedDurationHours(10.0)
                .build();

        trip.setRoute(route);

        // Setup Checkpoints
        TripCheckpoint pickup = TripCheckpoint.builder()
                .trip(trip)
                .name("Pickup: " + startAddr)
                .latitude(startLat)
                .longitude(startLng)
                .sequenceIndex(0)
                .type("PICKUP")
                .status("PENDING")
                .plannedArrivalTime(LocalDateTime.now().plusHours(2))
                .build();

        TripCheckpoint destination = TripCheckpoint.builder()
                .trip(trip)
                .name("Destination: " + endAddr)
                .latitude(endLat)
                .longitude(endLng)
                .sequenceIndex(1)
                .type("DESTINATION")
                .status("PENDING")
                .plannedArrivalTime(LocalDateTime.now().plusHours(10))
                .build();

        trip.getCheckpoints().add(pickup);
        trip.getCheckpoints().add(destination);

        Trip saved = tripRepository.save(trip);

        // Timeline log
        createTimelineEvent(saved.getId(), "TRIP_CREATED", "Trip Created",
                "Trip created automatically from match acceptance.", startLat, startLng);

        // Publish Event
        kafkaProducer.publishTripCreated(saved.getId(), saved.getShipmentId(), saved.getTruckId(), saved.getDriverId(), saved.getBusinessId());
        log.info("Successfully created Trip ID: {} from match acceptance", saved.getId());
    }

    @Override
    @Transactional
    public TripDto.Response createTripManual(TripDto.CreateRequest request) {
        // Validation via Feign
        validateClients(request.driverId(), request.truckId(), request.shipmentId(), request.businessId());

        boolean exists = tripRepository.existsByShipmentIdAndStatusNotAndIsDeletedFalse(
                request.shipmentId(), "CANCELLED");
        if (exists) {
            throw new IllegalArgumentException("Trip already exists and is active for shipment ID: " + request.shipmentId());
        }

        Trip trip = Trip.builder()
                .shipmentId(request.shipmentId())
                .truckId(request.truckId())
                .driverId(request.driverId())
                .businessId(request.businessId())
                .status("ASSIGNED")
                .totalDistanceTravelledKm(0.0)
                .remainingDistanceKm(request.route().plannedDistanceKm() != null ? request.route().plannedDistanceKm() : 100.0)
                .averageSpeedKmh(50.0)
                .build();

        TripRoute route = tripMapper.toEntity(request.route());
        route.setTrip(trip);
        trip.setRoute(route);

        if (request.checkpoints() != null) {
            for (CheckpointDto.CreateRequest cpReq : request.checkpoints()) {
                TripCheckpoint cp = tripMapper.toEntity(cpReq);
                cp.setTrip(trip);
                trip.getCheckpoints().add(cp);
            }
        } else {
            // Setup defaults
            TripCheckpoint pickup = TripCheckpoint.builder()
                    .trip(trip)
                    .name("Pickup: " + route.getStartAddress())
                    .latitude(route.getStartLatitude())
                    .longitude(route.getStartLongitude())
                    .sequenceIndex(0)
                    .type("PICKUP")
                    .status("PENDING")
                    .build();

            TripCheckpoint destination = TripCheckpoint.builder()
                    .trip(trip)
                    .name("Destination: " + route.getEndAddress())
                    .latitude(route.getEndLatitude())
                    .longitude(route.getEndLongitude())
                    .sequenceIndex(1)
                    .type("DESTINATION")
                    .status("PENDING")
                    .build();

            trip.getCheckpoints().add(pickup);
            trip.getCheckpoints().add(destination);
        }

        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "TRIP_CREATED", "Trip Created Manually",
                "Trip created manually by operator.", route.getStartLatitude(), route.getStartLongitude());

        kafkaProducer.publishTripCreated(saved.getId(), saved.getShipmentId(), saved.getTruckId(), saved.getDriverId(), saved.getBusinessId());
        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response assignDriverAndTruck(UUID tripId, TripDto.AssignRequest request) {
        Trip trip = findTrip(tripId);
        validateClients(request.driverId(), request.truckId(), trip.getShipmentId(), trip.getBusinessId());

        TripState state = getTripState(trip.getStatus());
        state.assign(trip); // validates transition and updates state to ASSIGNED

        trip.setDriverId(request.driverId());
        trip.setTruckId(request.truckId());
        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "STATUS_CHANGE", "Driver & Truck Assigned",
                "Assigned Driver: " + request.driverId() + ", Truck: " + request.truckId(),
                trip.getCurrentLatitude(), trip.getCurrentLongitude());

        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response setTripReady(UUID tripId) {
        Trip trip = findTrip(tripId);
        TripState state = getTripState(trip.getStatus());
        state.ready(trip); // transitions to READY

        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "STATUS_CHANGE", "Trip Ready",
                "Pre-trip verification completed. Trip is ready to start.",
                trip.getCurrentLatitude(), trip.getCurrentLongitude());

        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response startTrip(UUID tripId) {
        Trip trip = findTrip(tripId);
        TripState state = getTripState(trip.getStatus());
        state.start(trip); // transitions to STARTED

        trip.setStartedAt(LocalDateTime.now());
        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "STARTED", "Trip Started",
                "Trip has officially started.",
                trip.getRoute().getStartLatitude(), trip.getRoute().getStartLongitude());

        kafkaProducer.publishTripStarted(saved.getId(), saved.getShipmentId(), saved.getDriverId(), saved.getTruckId());
        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response pauseTrip(UUID tripId) {
        Trip trip = findTrip(tripId);
        TripState state = getTripState(trip.getStatus());
        state.pause(trip); // transitions to PAUSED

        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "PAUSED", "Trip Paused",
                "Trip execution is temporarily suspended.",
                trip.getCurrentLatitude(), trip.getCurrentLongitude());

        kafkaProducer.publishTripPaused(saved.getId());
        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response resumeTrip(UUID tripId) {
        Trip trip = findTrip(tripId);
        TripState state = getTripState(trip.getStatus());
        state.resume(trip); // transitions to RESUMED (or IN_PROGRESS)

        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "RESUMED", "Trip Resumed",
                "Trip execution has resumed.",
                trip.getCurrentLatitude(), trip.getCurrentLongitude());

        kafkaProducer.publishTripResumed(saved.getId());
        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response completeTrip(UUID tripId) {
        Trip trip = findTrip(tripId);
        TripState state = getTripState(trip.getStatus());
        state.complete(trip); // transitions to COMPLETED

        trip.setCompletedAt(LocalDateTime.now());
        trip.setRemainingDistanceKm(0.0);
        trip.setCurrentLatitude(trip.getRoute().getEndLatitude());
        trip.setCurrentLongitude(trip.getRoute().getEndLongitude());

        // Recalculate Timeline & Summarize
        TripSummary summary = buildAndSaveSummary(trip);
        trip.setSummary(summary);

        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "COMPLETED", "Trip Completed",
                "Trip completed successfully at destination.",
                trip.getRoute().getEndLatitude(), trip.getRoute().getEndLongitude());

        kafkaProducer.publishTripCompleted(saved.getId(), saved.getShipmentId(), saved.getDriverId(), saved.getTruckId(), saved.getTotalDistanceTravelledKm());
        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TripDto.Response cancelTrip(UUID tripId, String reason) {
        Trip trip = findTrip(tripId);
        TripState state = getTripState(trip.getStatus());
        state.cancel(trip); // transitions to CANCELLED

        Trip saved = tripRepository.save(trip);

        createTimelineEvent(saved.getId(), "CANCELLED", "Trip Cancelled",
                "Trip cancelled. Reason: " + reason,
                trip.getCurrentLatitude(), trip.getCurrentLongitude());

        kafkaProducer.publishTripCancelled(saved.getId(), reason);
        return tripMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void cancelTripForShipment(UUID shipmentId, String reason) {
        List<Trip> activeTrips = tripRepository.findAll().stream()
                .filter(t -> t.getShipmentId().equals(shipmentId)
                        && !t.getStatus().equals("COMPLETED")
                        && !t.getStatus().equals("CANCELLED")
                        && !t.isDeleted())
                .collect(Collectors.toList());

        for (Trip trip : activeTrips) {
            cancelTrip(trip.getId(), reason);
        }
    }

    @Override
    public TripDto.Response getTripById(UUID id) {
        return tripMapper.toResponse(findTrip(id));
    }

    @Override
    public Page<TripDto.Response> searchTrips(
            UUID tripId, UUID shipmentId, UUID truckId, UUID driverId, UUID businessId,
            String status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return tripRepository.searchTrips(tripId, shipmentId, truckId, driverId, businessId, status, startDate, endDate, pageable)
                .map(tripMapper::toResponse);
    }

    @Override
    public List<TripTimelineDto.Response> getTripTimeline(UUID tripId) {
        // Assert trip exists
        findTrip(tripId);
        return timelineRepository.findByTripIdAndIsDeletedFalseOrderByTimestampAsc(tripId).stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TripSummaryDto.Response getTripSummary(UUID tripId) {
        TripSummary summary = summaryRepository.findByTripIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip summary not found for Trip ID: " + tripId));
        return tripMapper.toDto(summary);
    }

    // Helper Methods
    private Trip findTrip(UUID id) {
        return tripRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + id));
    }

    private void validateClients(UUID driverId, UUID truckId, UUID shipmentId, UUID businessId) {
        try {
            userClient.getUser(driverId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Driver ID " + driverId + " does not exist or user-service is offline.");
        }

        try {
            userClient.getDriverStatus(driverId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Driver verification record missing for ID: " + driverId);
        }

        try {
            truckClient.getTruck(truckId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Truck ID " + truckId + " does not exist or truck-service is offline.");
        }

        try {
            shipmentClient.getShipment(shipmentId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Shipment ID " + shipmentId + " does not exist or shipment-service is offline.");
        }

        try {
            userClient.getUser(businessId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Business ID " + businessId + " does not exist or user-service is offline.");
        }
    }

    private void createTimelineEvent(UUID tripId, String eventType, String title, String description, Double lat, Double lng) {
        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .title(title)
                .description(description)
                .latitude(lat)
                .longitude(lng)
                .build();
        timelineRepository.save(timeline);
    }

    private TripSummary buildAndSaveSummary(Trip trip) {
        double durationHours = 0.0;
        if (trip.getStartedAt() != null && trip.getCompletedAt() != null) {
            durationHours = Duration.between(trip.getStartedAt(), trip.getCompletedAt()).toMinutes() / 60.0;
        }

        double totalFuelVol = trip.getFuelLogs().stream().mapToDouble(FuelLog::getFuelVolumeLiters).sum();
        double totalFuelCost = trip.getFuelLogs().stream().mapToDouble(FuelLog::getTotalCost).sum();

        int delayCount = trip.getDelays().size();
        double totalDelayMin = trip.getDelays().stream().mapToDouble(TripDelay::getDurationMinutes).sum();

        int restStopCount = trip.getRestStops().size();
        double totalRestMin = trip.getRestStops().stream()
                .mapToDouble(rs -> rs.getEndTime() != null ? Duration.between(rs.getStartTime(), rs.getEndTime()).toMinutes() : 0.0)
                .sum();

        double avgSpeed = 0.0;
        double distance = trip.getTotalDistanceTravelledKm() != null ? trip.getTotalDistanceTravelledKm() : 0.0;
        if (durationHours > 0.0) {
            avgSpeed = distance / durationHours;
        }

        TripSummary summary = TripSummary.builder()
                .trip(trip)
                .startActualTime(trip.getStartedAt())
                .endActualTime(trip.getCompletedAt())
                .durationHours(durationHours)
                .totalDistanceKm(distance)
                .averageSpeedKmh(avgSpeed)
                .totalFuelVolumeLiters(totalFuelVol)
                .totalFuelCost(totalFuelCost)
                .delayCount(delayCount)
                .totalDelayMinutes(totalDelayMin)
                .restStopCount(restStopCount)
                .totalRestMinutes(totalRestMin)
                .build();

        return summaryRepository.save(summary);
    }

    private TripState getTripState(String status) {
        return switch (status.toUpperCase()) {
            case "WAITING" -> new com.smartlogistics.trackingservice.service.state.WaitingState();
            case "ASSIGNED" -> new com.smartlogistics.trackingservice.service.state.AssignedState();
            case "READY" -> new com.smartlogistics.trackingservice.service.state.ReadyState();
            case "STARTED" -> new com.smartlogistics.trackingservice.service.state.StartedState();
            case "IN_PROGRESS" -> new com.smartlogistics.trackingservice.service.state.InProgressState();
            case "PAUSED" -> new com.smartlogistics.trackingservice.service.state.PausedState();
            case "RESUMED" -> new com.smartlogistics.trackingservice.service.state.ResumedState();
            case "COMPLETED" -> new com.smartlogistics.trackingservice.service.state.CompletedState();
            case "CANCELLED" -> new com.smartlogistics.trackingservice.service.state.CancelledState();
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }
}
