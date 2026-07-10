package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.TripDelayDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DelayService {
    TripDelayDto.Response addTripDelay(UUID tripId, TripDelayDto.CreateRequest request);
    TripDelayDto.Response resolveTripDelay(UUID tripId, UUID delayId, LocalDateTime endTime);
    List<TripDelayDto.Response> getTripDelays(UUID tripId);
}
