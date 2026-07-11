package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;

public interface ReportGeneratorStrategy {
    String getReportType(); // REVENUE, TRIP, TRUCK, DRIVER, BUSINESS, MATCHING, FUEL, CARBON
    ReportResponseDto generateReport(ReportRequestDto request);
}
