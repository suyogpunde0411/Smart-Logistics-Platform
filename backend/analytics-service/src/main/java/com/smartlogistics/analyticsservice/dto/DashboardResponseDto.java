package com.smartlogistics.analyticsservice.dto;

import java.util.Map;

public record DashboardResponseDto(
    String role,
    Map<String, Object> summaryMetrics,
    Map<String, Object> chartData,
    Map<String, Object> secondaryKpis
) {}
