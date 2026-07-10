package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.CheckpointDto;
import com.smartlogistics.trackingservice.entity.TripCheckpoint;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripTimeline;
import com.smartlogistics.trackingservice.exception.CheckpointException;
import com.smartlogistics.trackingservice.exception.TripNotFoundException;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.TripCheckpointRepository;
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
public class CheckpointServiceImpl implements CheckpointService {

    private final TripCheckpointRepository checkpointRepository;
    private final TripRepository tripRepository;
    private final TripTimelineRepository timelineRepository;
    private final TripMapper tripMapper;

    @Override
    @Transactional
    public CheckpointDto.Response addCheckpoint(UUID tripId, CheckpointDto.CreateRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));

        TripCheckpoint cp = tripMapper.toEntity(request);
        cp.setTrip(trip);
        TripCheckpoint saved = checkpointRepository.save(cp);

        createTimelineEvent(tripId, "CHECKPOINT_ADDED", "New Checkpoint Added",
                "Planned checkpoint added: " + cp.getName(), cp.getLatitude(), cp.getLongitude());

        return tripMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CheckpointDto.Response reachCheckpoint(UUID tripId, UUID checkpointId) {
        TripCheckpoint cp = checkpointRepository.findById(checkpointId)
                .orElseThrow(() -> new CheckpointException("Checkpoint not found with ID: " + checkpointId));

        if (!cp.getTrip().getId().equals(tripId)) {
            throw new CheckpointException("Checkpoint does not belong to specified Trip ID: " + tripId);
        }

        cp.setStatus("REACHED");
        cp.setActualArrivalTime(LocalDateTime.now());
        TripCheckpoint saved = checkpointRepository.save(cp);

        createTimelineEvent(tripId, "CHECKPOINT_REACHED", "Reached Checkpoint: " + cp.getName(),
                "Checkpoint reached: " + cp.getName(), cp.getLatitude(), cp.getLongitude());

        return tripMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CheckpointDto.Response departCheckpoint(UUID tripId, UUID checkpointId) {
        TripCheckpoint cp = checkpointRepository.findById(checkpointId)
                .orElseThrow(() -> new CheckpointException("Checkpoint not found with ID: " + checkpointId));

        if (!cp.getTrip().getId().equals(tripId)) {
            throw new CheckpointException("Checkpoint does not belong to specified Trip ID: " + tripId);
        }

        cp.setStatus("DEPARTED");
        cp.setDepartureTime(LocalDateTime.now());
        TripCheckpoint saved = checkpointRepository.save(cp);

        createTimelineEvent(tripId, "CHECKPOINT_DEPARTED", "Departed Checkpoint: " + cp.getName(),
                "Checkpoint departed: " + cp.getName(), cp.getLatitude(), cp.getLongitude());

        return tripMapper.toDto(saved);
    }

    @Override
    public List<CheckpointDto.Response> getCheckpoints(UUID tripId) {
        // Validate trip exists
        tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));
        return checkpointRepository.findByTripIdAndIsDeletedFalseOrderBySequenceIndexAsc(tripId).stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
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
