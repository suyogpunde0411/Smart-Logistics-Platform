package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.RevenueAnalytics;
import com.smartlogistics.analyticsservice.repository.RevenueAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RevenueReportGenerator implements ReportGeneratorStrategy {

    private final RevenueAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "REVENUE";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        LocalDate start = request.startDate() != null ? request.startDate().toLocalDate() : LocalDate.now().minusMonths(1);
        LocalDate end = request.endDate() != null ? request.endDate().toLocalDate() : LocalDate.now();

        List<RevenueAnalytics> data = repository.findByDateBetweenAndIsDeletedFalseOrderByDateAsc(start, end);

        double totalRevenue = 0.0;
        int totalShipments = 0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (RevenueAnalytics ra : data) {
            totalRevenue += ra.getRevenue();
            totalShipments += ra.getShipmentCount();

            Map<String, Object> row = new HashMap<>();
            row.put("date", ra.getDate());
            row.put("revenue", ra.getRevenue());
            row.put("shipments", ra.getShipmentCount());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRevenue", totalRevenue);
        summary.put("totalShipments", totalShipments);
        summary.put("averageRevenuePerDay", data.isEmpty() ? 0.0 : totalRevenue / data.size());

        // Generate CSV bytes as standard export interface
        StringBuilder csv = new StringBuilder("Date,Revenue,Shipments\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("date")).append(",")
               .append(r.get("revenue")).append(",")
               .append(r.get("shipments")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("REVENUE", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
