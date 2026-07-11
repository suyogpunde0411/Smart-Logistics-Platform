package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.FuelAnalytics;
import com.smartlogistics.analyticsservice.repository.FuelAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FuelReportGenerator implements ReportGeneratorStrategy {

    private final FuelAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "FUEL";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        List<FuelAnalytics> list = repository.findAll();

        double totalFuel = 0.0;
        double totalDistance = 0.0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (FuelAnalytics fa : list) {
            totalFuel += fa.getTotalFuelLiters();
            totalDistance += fa.getTotalDistanceKm();

            Map<String, Object> row = new HashMap<>();
            row.put("truckType", fa.getTruckType());
            row.put("routeKey", fa.getRouteKey());
            row.put("totalFuelLiters", fa.getTotalFuelLiters());
            row.put("totalDistanceKm", fa.getTotalDistanceKm());
            row.put("averageFuelPerKm", fa.getAverageFuelPerKm());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalFuelConsumedLiters", totalFuel);
        summary.put("totalDistanceCoveredKm", totalDistance);
        summary.put("averageFuelEfficiencyLitersPerKm", totalDistance == 0 ? 0.0 : totalFuel / totalDistance);

        StringBuilder csv = new StringBuilder("TruckType,RouteKey,TotalFuelLiters,TotalDistanceKm,AverageFuelPerKm\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("truckType")).append(",")
               .append(r.get("routeKey")).append(",")
               .append(r.get("totalFuelLiters")).append(",")
               .append(r.get("totalDistanceKm")).append(",")
               .append(r.get("averageFuelPerKm")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("FUEL", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
