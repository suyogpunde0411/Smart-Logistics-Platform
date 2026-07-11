package com.smartlogistics.analyticsservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ReportResponseDto(
    String reportType,
    LocalDateTime generatedAt,
    Map<String, Object> summaryMetrics,
    List<Map<String, Object>> dataRows,
    byte[] exportedData
) {}
