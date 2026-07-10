package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.GpsLocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GpsTrackingService {
    GpsLocationDto.Response recordGpsLocation(UUID tripId, GpsLocationDto.CreateRequest request);
    Page<GpsLocationDto.Response> getGpsLocationHistory(UUID tripId, Pageable pageable);
    List<GpsLocationDto.Response> getRouteReplay(UUID tripId);
}
