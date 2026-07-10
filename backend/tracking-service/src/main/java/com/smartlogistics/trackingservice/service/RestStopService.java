package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.RestStopDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RestStopService {
    RestStopDto.Response startRestStop(UUID tripId, RestStopDto.CreateRequest request);
    RestStopDto.Response endRestStop(UUID tripId, UUID restStopId, LocalDateTime endTime);
    List<RestStopDto.Response> getRestStops(UUID tripId);
}
