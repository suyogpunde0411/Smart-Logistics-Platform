package com.smartlogistics.analyticsservice.controller;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics/reports")
@RequiredArgsConstructor
@Tag(name = "Analytics Reporting", description = "Endpoints for generating structured BI reports")
public class ReportController {

    private final AnalyticsService service;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Generate a structured analytics report (JSON format)")
    public ResponseEntity<ReportResponseDto> generateReport(@RequestBody ReportRequestDto request) {
        return ResponseEntity.ok(service.generateReport(request));
    }

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Export a structured analytics report (CSV/PDF bytes download)")
    public ResponseEntity<byte[]> exportReport(@RequestBody ReportRequestDto request) {
        ReportResponseDto report = service.generateReport(request);
        byte[] data = report.exportedData();

        String format = request.format() != null ? request.format().toLowerCase() : "csv";
        String contentType = "text/csv";
        String extension = ".csv";

        if ("pdf".equals(format)) {
            contentType = "application/pdf";
            extension = ".pdf";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + report.reportType().toLowerCase() + "_report" + extension)
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}
