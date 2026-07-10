package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.RestStopDto;
import com.smartlogistics.trackingservice.entity.RestStop;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripTimeline;
import com.smartlogistics.trackingservice.exception.TripNotFoundException;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.RestStopRepository;
import com.smartlogistics.trackingservice.repository.TripRepository;
import com.smartlogistics.trackingservice.repository.TripTimelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestStopServiceImpl implements RestStopService {

    private final RestStopRepository restStopRepository;
    private final TripRepository tripRepository;
    private final TripTimelineRepository timelineRepository;
    private final TripService tripService;
    private final TripMapper tripMapper;

    @Override
    @Transactional
    public RestStopDto.Response startRestStop(UUID tripId, RestStopDto.CreateRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));

        RestStop restStop = RestStop.builder()
                .trip(trip)
                .startTime(request.startTime() != null ? request.startTime() : LocalDateTime.now())
                .latitude(request.latitude() != null ? request.latitude() : trip.getCurrentLatitude())
                .longitude(request.longitude() != null ? request.longitude() : trip.getCurrentLongitude())
                .stopLocationName(request.stopLocationName())
                .notes(request.notes())
                .build();

        RestStop saved = restStopRepository.save(restStop);

        // Pause the trip automatically when a rest stop starts
        try {
            tripService.pauseTrip(tripId);
        } catch (Exception e) {
            log.warn("Could not transition trip state to PAUSED for rest stop start: {}", e.getMessage());
        }

        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType("REST_START")
                .timestamp(LocalDateTime.now())
                .title("Rest Stop Started")
                .description("Location: " + request.stopLocationName() + ", Notes: " + request.notes())
                .latitude(restStop.getLatitude())
                .longitude(restStop.getLongitude())
                .build();
        timelineRepository.save(timeline);

        return tripMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RestStopDto.Response endRestStop(UUID tripId, UUID restStopId, LocalDateTime endTime) {
        RestStop restStop = restStopRepository.findById(restStopId)
                .orElseThrow(() -> new IllegalArgumentException("Rest stop not found with ID: " + restStopId));

        if (!restStop.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException("Rest stop does not belong to specified Trip ID: " + tripId);
        }

        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        restStop.setEndTime(end);
        RestStop saved = restStopRepository.save(restStop);

        // Resume the trip automatically when a rest stop ends
        try {
            tripService.resumeTrip(tripId);
        } catch (Exception e) {
            log.warn("Could not transition trip state to RESUMED for rest stop end: {}", e.getMessage());
        }

        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType("REST_END")
                .timestamp(LocalDateTime.now())
                .title("Rest Stop Ended")
                .description("Rest stop ended. Location: " + restStop.getStopLocationName())
                .latitude(restStop.getLatitude())
                .longitude(restStop.getLongitude())
                .build();
        timelineRepository.save(timeline);

        return tripMapper.toDto(saved);
    }

    @Override
    public List<RestStopDto.Response> getRestStops(UUID tripId) {
        tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));
        return restStopRepository.findByTripIdAndIsDeletedFalseOrderByStartTimeDesc(tripId).stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }
}
