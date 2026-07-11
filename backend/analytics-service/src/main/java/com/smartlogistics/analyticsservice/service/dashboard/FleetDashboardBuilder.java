package com.smartlogistics.analyticsservice.service.dashboard;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.entity.FleetAnalytics;
import com.smartlogistics.analyticsservice.repository.FleetAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FleetDashboardBuilder implements DashboardBuilder {

    private final FleetAnalyticsRepository fleetAnalyticsRepository;
    
    private final Map<String, Object> summaryMetrics = new HashMap<>();
    private final Map<String, Object> chartData = new HashMap<>();
    private final Map<String, Object> secondaryKpis = new HashMap<>();

    @Override
    public DashboardBuilder buildSummaryMetrics(UUID entityId) {
        if (entityId != null) {
            Optional<FleetAnalytics> fa = fleetAnalyticsRepository.findByFleetOwnerIdAndIsDeletedFalse(entityId);
            fa.ifPresent(f -> {
                summaryMetrics.put("totalTrucks", f.getTotalTrucks());
                summaryMetrics.put("activeTrucks", f.getActiveTrucks());
                summaryMetrics.put("totalTrips", f.getTotalTrips());
            });
        }
        return this;
    }

    @Override
    public DashboardBuilder buildChartData(UUID entityId) {
        chartData.put("labels", Map.of("Active", 8, "Idle", 2));
        return this;
    }

    @Override
    public DashboardBuilder buildSecondaryKpis(UUID entityId) {
        if (entityId != null) {
            Optional<FleetAnalytics> fa = fleetAnalyticsRepository.findByFleetOwnerIdAndIsDeletedFalse(entityId);
            fa.ifPresent(f -> {
                secondaryKpis.put("utilizationRate", f.getUtilizationRate());
                secondaryKpis.put("emptyTripsCount", f.getEmptyTripsCount());
                secondaryKpis.put("emptyTripReductionPercent", f.getEmptyTripReductionPercent());
            });
        }
        return this;
    }

    @Override
    public DashboardResponseDto getResult() {
        return new DashboardResponseDto("FLEET_OWNER", new HashMap<>(summaryMetrics), new HashMap<>(chartData), new HashMap<>(secondaryKpis));
    }
}
