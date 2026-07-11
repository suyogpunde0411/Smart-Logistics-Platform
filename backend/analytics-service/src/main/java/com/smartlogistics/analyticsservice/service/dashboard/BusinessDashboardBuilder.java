package com.smartlogistics.analyticsservice.service.dashboard;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.entity.BusinessAnalytics;
import com.smartlogistics.analyticsservice.repository.BusinessAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BusinessDashboardBuilder implements DashboardBuilder {

    private final BusinessAnalyticsRepository businessAnalyticsRepository;
    
    private final Map<String, Object> summaryMetrics = new HashMap<>();
    private final Map<String, Object> chartData = new HashMap<>();
    private final Map<String, Object> secondaryKpis = new HashMap<>();

    @Override
    public DashboardBuilder buildSummaryMetrics(UUID entityId) {
        if (entityId != null) {
            Optional<BusinessAnalytics> ba = businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(entityId);
            ba.ifPresent(b -> {
                summaryMetrics.put("totalShipments", b.getTotalShipments());
                summaryMetrics.put("completedShipments", b.getCompletedShipments());
                summaryMetrics.put("cancelledShipments", b.getCancelledShipments());
                summaryMetrics.put("totalBudgetSpent", b.getTotalBudgetSpent());
            });
        }
        return this;
    }

    @Override
    public DashboardBuilder buildChartData(UUID entityId) {
        chartData.put("spendLabels", Map.of("Week1", 1200, "Week2", 1500));
        return this;
    }

    @Override
    public DashboardBuilder buildSecondaryKpis(UUID entityId) {
        if (entityId != null) {
            Optional<BusinessAnalytics> ba = businessAnalyticsRepository.findByBusinessIdAndIsDeletedFalse(entityId);
            ba.ifPresent(b -> {
                secondaryKpis.put("averageRating", b.getAverageRating());
            });
        }
        return this;
    }

    @Override
    public DashboardResponseDto getResult() {
        return new DashboardResponseDto("BUSINESS_OWNER", new HashMap<>(summaryMetrics), new HashMap<>(chartData), new HashMap<>(secondaryKpis));
    }
}
