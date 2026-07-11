package com.smartlogistics.analyticsservice.service.dashboard;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.entity.DriverAnalytics;
import com.smartlogistics.analyticsservice.repository.DriverAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DriverDashboardBuilder implements DashboardBuilder {

    private final DriverAnalyticsRepository driverAnalyticsRepository;
    
    private final Map<String, Object> summaryMetrics = new HashMap<>();
    private final Map<String, Object> chartData = new HashMap<>();
    private final Map<String, Object> secondaryKpis = new HashMap<>();

    @Override
    public DashboardBuilder buildSummaryMetrics(UUID entityId) {
        if (entityId != null) {
            Optional<DriverAnalytics> da = driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(entityId);
            da.ifPresent(d -> {
                summaryMetrics.put("totalTrips", d.getTotalTrips());
                summaryMetrics.put("completedTrips", d.getCompletedTrips());
                summaryMetrics.put("cancelledTrips", d.getCancelledTrips());
                summaryMetrics.put("totalRevenue", d.getTotalRevenue());
            });
        }
        return this;
    }

    @Override
    public DashboardBuilder buildChartData(UUID entityId) {
        chartData.put("labels", Map.of("Monday", 2, "Tuesday", 4, "Wednesday", 1));
        return this;
    }

    @Override
    public DashboardBuilder buildSecondaryKpis(UUID entityId) {
        if (entityId != null) {
            Optional<DriverAnalytics> da = driverAnalyticsRepository.findByDriverIdAndIsDeletedFalse(entityId);
            da.ifPresent(d -> {
                secondaryKpis.put("averageRating", d.getAverageRating());
                secondaryKpis.put("trustScore", d.getTrustScore());
                secondaryKpis.put("lateDeliveries", d.getLateDeliveries());
            });
        }
        return this;
    }

    @Override
    public DashboardResponseDto getResult() {
        return new DashboardResponseDto("DRIVER", new HashMap<>(summaryMetrics), new HashMap<>(chartData), new HashMap<>(secondaryKpis));
    }
}
