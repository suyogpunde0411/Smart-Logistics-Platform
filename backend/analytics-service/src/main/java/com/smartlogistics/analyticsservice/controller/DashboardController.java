package com.smartlogistics.analyticsservice.controller;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.dto.TripAnalyticsResponseDto;
import com.smartlogistics.analyticsservice.service.AnalyticsService;
import com.smartlogistics.shared.security.CurrentUserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics/dashboard")
@RequiredArgsConstructor
@Tag(name = "Analytics Dashboard", description = "Endpoints for retrieving aggregated BI dashboard stats")
public class DashboardController {

    private final AnalyticsService service;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get the dynamic dashboard summary filtered by role and current user")
    public ResponseEntity<DashboardResponseDto> getDashboardSummary() {
        UUID userId = CurrentUserUtil.getUserId();
        String role = getPrimaryRole();
        return ResponseEntity.ok(service.getDashboard(role, userId));
    }

    @GetMapping("/kpis")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin KPI metrics (Admin only)")
    public ResponseEntity<Map<String, Object>> getKpis() {
        return ResponseEntity.ok(service.getKpis());
    }

    @GetMapping("/top-routes")
    @PreAuthorize("hasAnyRole('ADMIN', 'FLEET_OWNER')")
    @Operation(summary = "Get the list of top routes by trip volume")
    public ResponseEntity<List<Map<String, Object>>> getTopRoutes() {
        return ResponseEntity.ok(service.getTopRoutes());
    }

    @GetMapping("/trips")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Search and filter detailed historical trip logs")
    public ResponseEntity<Page<TripAnalyticsResponseDto>> searchTrips(
            @RequestParam(required = false) UUID driverId,
            @RequestParam(required = false) UUID businessId,
            @RequestParam(required = false) UUID truckId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        UUID currentUserId = CurrentUserUtil.getUserId();
        String primaryRole = getPrimaryRole();

        // Enforce Driver isolation: drivers can only view their own analytics records
        UUID targetDriverId = driverId;
        if ("DRIVER".equalsIgnoreCase(primaryRole)) {
            targetDriverId = currentUserId;
        }

        return ResponseEntity.ok(service.searchTrips(
                targetDriverId, businessId, truckId, status, city, startDate, endDate, pageable));
    }

    private String getPrimaryRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .findFirst()
                .orElse("DRIVER");
    }
}
