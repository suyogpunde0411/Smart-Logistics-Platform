package com.smartlogistics.analyticsservice.service;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.dto.TripAnalyticsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AnalyticsService {
    DashboardResponseDto getDashboard(String role, UUID entityId);
    ReportResponseDto generateReport(ReportRequestDto request);
    Page<TripAnalyticsResponseDto> searchTrips(
            UUID driverId, UUID businessId, UUID truckId, String status, String city,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Map<String, Object>> getTopRoutes();
    Map<String, Object> getKpis();
}
