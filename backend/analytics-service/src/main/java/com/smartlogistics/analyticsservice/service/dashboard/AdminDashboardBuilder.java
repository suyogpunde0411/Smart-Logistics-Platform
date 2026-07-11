package com.smartlogistics.analyticsservice.service.dashboard;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.entity.DashboardMetric;
import com.smartlogistics.analyticsservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminDashboardBuilder implements DashboardBuilder {

    private final DashboardMetricRepository dashboardMetricRepository;
    private final MatchingAnalyticsRepository matchingAnalyticsRepository;
    
    private final Map<String, Object> summaryMetrics = new HashMap<>();
    private final Map<String, Object> chartData = new HashMap<>();
    private final Map<String, Object> secondaryKpis = new HashMap<>();

    @Override
    public DashboardBuilder buildSummaryMetrics(UUID entityId) {
        List<DashboardMetric> metrics = dashboardMetricRepository.findAll();
        for (DashboardMetric dm : metrics) {
            summaryMetrics.put(dm.getMetricKey(), dm.getMetricValue());
        }
        return this;
    }

    @Override
    public DashboardBuilder buildChartData(UUID entityId) {
        // Mock some analytics trends for charts
        chartData.put("labels", List.of("Jan", "Feb", "Mar", "Apr", "May"));
        chartData.put("revenueTrend", List.of(12000, 19000, 3000, 5000, 2000));
        return this;
    }

    @Override
    public DashboardBuilder buildSecondaryKpis(UUID entityId) {
        matchingAnalyticsRepository.findLatestAndIsDeletedFalse().ifPresent(ma -> {
            secondaryKpis.put("matchingSuccessRate", ma.getSuccessRate());
            secondaryKpis.put("totalMatchesCreated", ma.getTotalMatchesCreated());
        });
        return this;
    }

    @Override
    public DashboardResponseDto getResult() {
        return new DashboardResponseDto("ADMIN", new HashMap<>(summaryMetrics), new HashMap<>(chartData), new HashMap<>(secondaryKpis));
    }
}
