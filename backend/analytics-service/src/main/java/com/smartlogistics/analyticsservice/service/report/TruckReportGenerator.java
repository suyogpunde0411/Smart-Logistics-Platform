package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.TruckAnalytics;
import com.smartlogistics.analyticsservice.repository.TruckAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TruckReportGenerator implements ReportGeneratorStrategy {

    private final TruckAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "TRUCK";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        List<TruckAnalytics> trucks;
        if (request.truckId() != null) {
            Optional<TruckAnalytics> single = repository.findByTruckIdAndIsDeletedFalse(request.truckId());
            trucks = single.isPresent() ? List.of(single.get()) : List.of();
        } else {
            trucks = repository.findAll();
        }

        double totalDistance = 0.0;
        double totalFuel = 0.0;
        double avgUtilizationSum = 0.0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (TruckAnalytics ta : trucks) {
            totalDistance += ta.getTotalDistanceKm();
            totalFuel += ta.getFuelConsumedLiters();
            avgUtilizationSum += ta.getUtilizationRate();

            Map<String, Object> row = new HashMap<>();
            row.put("truckId", ta.getTruckId());
            row.put("licensePlate", ta.getLicensePlate());
            row.put("totalTrips", ta.getTotalTrips());
            row.put("distanceKm", ta.getTotalDistanceKm());
            row.put("utilizationRate", ta.getUtilizationRate());
            row.put("fuelLiters", ta.getFuelConsumedLiters());
            row.put("carbonSavingsKg", ta.getCarbonSavingsKg());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalTrucksTracked", trucks.size());
        summary.put("aggregatedDistanceKm", totalDistance);
        summary.put("aggregatedFuelLiters", totalFuel);
        summary.put("averageUtilizationPercent", trucks.isEmpty() ? 0.0 : avgUtilizationSum / trucks.size());

        StringBuilder csv = new StringBuilder("TruckId,LicensePlate,TotalTrips,DistanceKm,UtilizationRate,FuelLiters,CarbonSavingsKg\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("truckId")).append(",")
               .append(r.get("licensePlate")).append(",")
               .append(r.get("totalTrips")).append(",")
               .append(r.get("distanceKm")).append(",")
               .append(r.get("utilizationRate")).append(",")
               .append(r.get("fuelLiters")).append(",")
               .append(r.get("carbonSavingsKg")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("TRUCK", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
