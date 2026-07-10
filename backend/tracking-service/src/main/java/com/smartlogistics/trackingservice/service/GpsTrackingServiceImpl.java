package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.GpsLocationDto;
import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripCheckpoint;
import com.smartlogistics.trackingservice.entity.TripTimeline;
import com.smartlogistics.trackingservice.exception.GpsValidationException;
import com.smartlogistics.trackingservice.exception.InvalidTripStateException;
import com.smartlogistics.trackingservice.exception.TripNotFoundException;
import com.smartlogistics.trackingservice.events.TrackingKafkaProducer;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.GpsLocationRepository;
import com.smartlogistics.trackingservice.repository.TripCheckpointRepository;
import com.smartlogistics.trackingservice.repository.TripRepository;
import com.smartlogistics.trackingservice.repository.TripTimelineRepository;
import com.smartlogistics.trackingservice.service.strategy.*;
import com.smartlogistics.trackingservice.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GpsTrackingServiceImpl implements GpsTrackingService {

    private final TripRepository tripRepository;
    private final GpsLocationRepository gpsLocationRepository;
    private final TripCheckpointRepository checkpointRepository;
    private final TripTimelineRepository timelineRepository;
    private final TrackingKafkaProducer kafkaProducer;
    private final TripMapper tripMapper;

    @Value("${tracking.eta.strategy:AVERAGE_SPEED}")
    private String etaStrategyName;

    @Value("${tracking.eta.default-speed-kmh:50.0}")
    private double defaultSpeedKmh;

    @Override
    @Transactional
    public GpsLocationDto.Response recordGpsLocation(UUID tripId, GpsLocationDto.CreateRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));

        validateGpsRequest(request);

        String status = trip.getStatus().toUpperCase();
        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            throw new InvalidTripStateException("Cannot record GPS location for " + status + " trip.");
        }

        // Automatic state transition to IN_PROGRESS upon GPS signal
        if (status.equals("STARTED") || status.equals("RESUMED")) {
            trip.setStatus("IN_PROGRESS");
            createTimelineEvent(tripId, "STATUS_CHANGE", "Trip In Progress",
                    "Telemetry signal received. Trip is now actively en route.",
                    request.latitude(), request.longitude());
        }

        // Calculate delta distance
        double deltaDistance = 0.0;
        List<GpsLocation> history = gpsLocationRepository.findByTripIdAndIsDeletedFalseOrderByTimestampAsc(tripId);
        if (!history.isEmpty()) {
            GpsLocation lastLoc = history.get(history.size() - 1);
            deltaDistance = GeoUtils.calculateDistance(
                    lastLoc.getLatitude(), lastLoc.getLongitude(),
                    request.latitude(), request.longitude()
            );
        }

        // Build GPS entity
        GpsLocation gpsLocation = GpsLocation.builder()
                .trip(trip)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .speedKmh(request.speedKmh())
                .heading(request.heading())
                .accuracy(request.accuracy())
                .altitude(request.altitude())
                .distanceTravelledKm(deltaDistance)
                .timestamp(request.timestamp() != null ? request.timestamp() : LocalDateTime.now())
                .build();

        // Update Trip stats
        double currentTotal = trip.getTotalDistanceTravelledKm() != null ? trip.getTotalDistanceTravelledKm() : 0.0;
        trip.setTotalDistanceTravelledKm(currentTotal + deltaDistance);
        trip.setCurrentLatitude(request.latitude());
        trip.setCurrentLongitude(request.longitude());
        trip.setLastGpsUpdatedAt(LocalDateTime.now());

        // Perform ETA Calculation
        double plannedDistance = trip.getRoute() != null && trip.getRoute().getPlannedDistanceKm() != null
                ? trip.getRoute().getPlannedDistanceKm() : 500.0;
        double remainingDistance = Math.max(0.0, plannedDistance - trip.getTotalDistanceTravelledKm());
        trip.setRemainingDistanceKm(remainingDistance);

        EtaCalculationStrategy strategy = getEtaStrategy(etaStrategyName);
        EtaCalculationResult etaResult = strategy.calculateEta(trip, gpsLocation, remainingDistance, defaultSpeedKmh);

        trip.setExpectedArrivalTime(etaResult.expectedArrival());
        trip.setAverageSpeedKmh(etaResult.averageSpeedKmh());

        // Save
        GpsLocation savedLoc = gpsLocationRepository.save(gpsLocation);
        Trip savedTrip = tripRepository.save(trip);

        // Proximity checks for checkpoints
        checkCheckpointProximity(savedTrip, request.latitude(), request.longitude());

        // Publish Events
        kafkaProducer.publishGpsUpdated(tripId, request.latitude(), request.longitude(), request.speedKmh(), request.heading(), savedTrip.getTotalDistanceTravelledKm());
        kafkaProducer.publishEtaUpdated(tripId, etaResult.expectedArrival(), remainingDistance, etaResult.remainingTimeHours());

        return tripMapper.toDto(savedLoc);
    }

    @Override
    public Page<GpsLocationDto.Response> getGpsLocationHistory(UUID tripId, Pageable pageable) {
        tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));
        return gpsLocationRepository.findByTripIdAndIsDeletedFalseOrderByTimestampDesc(tripId, pageable)
                .map(tripMapper::toDto);
    }

    @Override
    public List<GpsLocationDto.Response> getRouteReplay(UUID tripId) {
        tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));
        return gpsLocationRepository.findByTripIdAndIsDeletedFalseOrderByTimestampAsc(tripId).stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }

    // Helper Methods
    private void validateGpsRequest(GpsLocationDto.CreateRequest request) {
        if (request.latitude() < -90.0 || request.latitude() > 90.0) {
            throw new GpsValidationException("Latitude must be between -90 and 90 degrees.");
        }
        if (request.longitude() < -180.0 || request.longitude() > 180.0) {
            throw new GpsValidationException("Longitude must be between -180 and 180 degrees.");
        }
        if (request.speedKmh() != null && request.speedKmh() < 0.0) {
            throw new GpsValidationException("Speed cannot be negative.");
        }
    }

    private EtaCalculationStrategy getEtaStrategy(String name) {
        if ("DEFAULT_TRAFFIC".equalsIgnoreCase(name)) {
            return new DefaultTrafficEtaStrategy();
        }
        return new AverageSpeedEtaStrategy();
    }

    private void checkCheckpointProximity(Trip trip, double lat, double lng) {
        List<TripCheckpoint> checkpoints = checkpointRepository.findByTripIdAndIsDeletedFalseOrderBySequenceIndexAsc(trip.getId());
        for (TripCheckpoint cp : checkpoints) {
            if ("PENDING".equals(cp.getStatus())) {
                double distance = GeoUtils.calculateDistance(lat, lng, cp.getLatitude(), cp.getLongitude());
                // Proximity threshold of 200 meters (0.2 km)
                if (distance <= 0.2) {
                    cp.setStatus("REACHED");
                    cp.setActualArrivalTime(LocalDateTime.now());
                    checkpointRepository.save(cp);

                    createTimelineEvent(trip.getId(), "CHECKPOINT_REACHED", "Checkpoint Reached: " + cp.getName(),
                            "Vehicle arrived at checkpoint " + cp.getName() + " (Proximity: " + String.format("%.2f", distance * 1000) + "m)",
                            lat, lng);
                }
            }
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
}
