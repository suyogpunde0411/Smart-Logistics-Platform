package com.smartlogistics.analyticsservice.service.dashboard;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import java.util.UUID;

public interface DashboardBuilder {
    DashboardBuilder buildSummaryMetrics(UUID entityId);
    DashboardBuilder buildChartData(UUID entityId);
    DashboardBuilder buildSecondaryKpis(UUID entityId);
    DashboardResponseDto getResult();
}
