package com.smartlogistics.trackingservice.mapper;

import com.smartlogistics.trackingservice.dto.CheckpointDto;
import com.smartlogistics.trackingservice.dto.FuelLogDto;
import com.smartlogistics.trackingservice.dto.GpsLocationDto;
import com.smartlogistics.trackingservice.dto.RestStopDto;
import com.smartlogistics.trackingservice.dto.TripDelayDto;
import com.smartlogistics.trackingservice.dto.TripDto;
import com.smartlogistics.trackingservice.dto.TripRouteDto;
import com.smartlogistics.trackingservice.dto.TripSummaryDto;
import com.smartlogistics.trackingservice.dto.TripTimelineDto;
import com.smartlogistics.trackingservice.entity.FuelLog;
import com.smartlogistics.trackingservice.entity.GpsLocation;
import com.smartlogistics.trackingservice.entity.RestStop;
import com.smartlogistics.trackingservice.entity.Trip;
import com.smartlogistics.trackingservice.entity.TripCheckpoint;
import com.smartlogistics.trackingservice.entity.TripDelay;
import com.smartlogistics.trackingservice.entity.TripRoute;
import com.smartlogistics.trackingservice.entity.TripSummary;
import com.smartlogistics.trackingservice.entity.TripTimeline;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripMapper {
    TripDto.Response toResponse(Trip trip);
    TripRouteDto.Response toResponse(TripRoute route);
    CheckpointDto.Response toDto(TripCheckpoint checkpoint);
    GpsLocationDto.Response toDto(GpsLocation gpsLocation);
    FuelLogDto.Response toDto(FuelLog fuelLog);
    RestStopDto.Response toDto(RestStop restStop);
    TripDelayDto.Response toDto(TripDelay delay);
    TripSummaryDto.Response toDto(TripSummary summary);
    TripTimelineDto.Response toDto(TripTimeline timeline);

    TripRoute toEntity(TripRouteDto.CreateRequest request);
    TripCheckpoint toEntity(CheckpointDto.CreateRequest request);
    GpsLocation toEntity(GpsLocationDto.CreateRequest request);
}
