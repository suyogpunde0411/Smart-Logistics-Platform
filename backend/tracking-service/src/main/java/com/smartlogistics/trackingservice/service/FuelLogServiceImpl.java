package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.FuelLogDto;
import com.smartlogistics.trackingservice.entity.FuelLog;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.exception.TripNotFoundException;
import com.smartlogistics.trackingservice.mapper.TripMapper;
import com.smartlogistics.trackingservice.repository.FuelLogRepository;
import com.smartlogistics.trackingservice.repository.TripRepository;
import com.smartlogistics.trackingservice.repository.TripTimelineRepository;
import com.smartlogistics.trackingservice.entity.TripTimeline;
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
public class FuelLogServiceImpl implements FuelLogService {

    private final FuelLogRepository fuelLogRepository;
    private final TripRepository tripRepository;
    private final TripTimelineRepository timelineRepository;
    private final TripMapper tripMapper;

    @Override
    @Transactional
    public FuelLogDto.Response addFuelLog(UUID tripId, FuelLogDto.CreateRequest request) {
        Trip trip = tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));

        double totalCost = request.fuelVolumeLiters() * request.costPerLiter();

        FuelLog logEntry = FuelLog.builder()
                .trip(trip)
                .fuelVolumeLiters(request.fuelVolumeLiters())
                .costPerLiter(request.costPerLiter())
                .totalCost(totalCost)
                .odometerKm(request.odometerKm())
                .stationName(request.stationName())
                .stationLocation(request.stationLocation())
                .refueledAt(request.refueledAt() != null ? request.refueledAt() : LocalDateTime.now())
                .build();

        FuelLog saved = fuelLogRepository.save(logEntry);

        TripTimeline timeline = TripTimeline.builder()
                .tripId(tripId)
                .eventType("FUEL_REFUEL")
                .timestamp(LocalDateTime.now())
                .title("Refueled")
                .description("Volume: " + request.fuelVolumeLiters() + "L at station " + request.stationName())
                .latitude(trip.getCurrentLatitude())
                .longitude(trip.getCurrentLongitude())
                .build();
        timelineRepository.save(timeline);

        return tripMapper.toDto(saved);
    }

    @Override
    public List<FuelLogDto.Response> getFuelLogs(UUID tripId) {
        tripRepository.findByIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found with ID: " + tripId));
        return fuelLogRepository.findByTripIdAndIsDeletedFalseOrderByRefueledAtDesc(tripId).stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }
}
