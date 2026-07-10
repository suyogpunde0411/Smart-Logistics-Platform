package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.TripDelayDto;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripDelay;
import com.smartlogistics.trackingservice.entity.TripTimeline;
import com.smartlogistics.trackingservice.exception.TripNotFoundException;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.TripDelayRepository;
import com.smartlogistics.trackingservice.repository.TripRepository;
import com.smartlogistics.trackingservice.repository.TripTimelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DelayServiceImpl implements DelayService {

    private final TripDelayRepository delayRepository;
    private final TripRepository tripRepository;
    private final TripTimelineRepository timelineRepository;
    private final TripMapper tripMapper;

    @Override
    @Transactional
    public TripDelayDto.Response addTripDelay(UUID tripId, TripDelayDto.CreateRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));

        TripDelay delay = TripDelay.builder()
                .trip(trip)
                .startTime(request.startTime() != null ? request.startTime() : LocalDateTime.now())
                .category(request.category())
                .reason(request.reason())
                .latitude(request.latitude() != null ? request.latitude() : trip.getCurrentLatitude())
                .longitude(request.longitude() != null ? request.longitude() : trip.getCurrentLongitude())
                .build();

        TripDelay saved = delayRepository.save(delay);

        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType("DELAY")
                .timestamp(LocalDateTime.now())
                .title("Trip Delayed")
                .description("Category: " + request.category() + ", Reason: " + request.reason())
                .latitude(delay.getLatitude())
                .longitude(delay.getLongitude())
                .build();
        timelineRepository.save(timeline);

        return tripMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TripDelayDto.Response resolveTripDelay(UUID tripId, UUID delayId, LocalDateTime endTime) {
        TripDelay delay = delayRepository.findById(delayId)
                .orElseThrow(() -> new IllegalArgumentException("Trip delay not found with ID: " + delayId));

        if (!delay.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException("Trip delay does not belong to specified Trip ID: " + tripId);
        }

        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        delay.setEndTime(end);

        double duration = Duration.between(delay.getStartTime(), end).toMinutes();
        delay.setDurationMinutes(duration);

        TripDelay saved = delayRepository.save(delay);

        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType("DELAY_RESOLVED")
                .timestamp(LocalDateTime.now())
                .title("Delay Resolved")
                .description("Delay category " + delay.getCategory() + " resolved. Duration: " + duration + " min")
                .latitude(delay.getLatitude())
                .longitude(delay.getLongitude())
                .build();
        timelineRepository.save(timeline);

        return tripMapper.toDto(saved);
    }

    @Override
    public List<TripDelayDto.Response> getTripDelays(UUID tripId) {
        tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));
        return delayRepository.findByTripIdAndIsDeletedFalseOrderByStartTimeDesc(tripId).stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }
}
