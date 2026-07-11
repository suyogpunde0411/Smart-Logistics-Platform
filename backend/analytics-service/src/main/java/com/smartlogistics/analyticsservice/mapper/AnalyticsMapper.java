package com.smartlogistics.analyticsservice.mapper;

import com.smartlogistics.analyticsservice.entity.TripAnalytics;
import com.smartlogistics.analyticsservice.dto.TripAnalyticsResponseDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {
    TripAnalyticsResponseDto toDto(TripAnalytics entity);
    List<TripAnalyticsResponseDto> toDtoList(List<TripAnalytics> entities);
}
