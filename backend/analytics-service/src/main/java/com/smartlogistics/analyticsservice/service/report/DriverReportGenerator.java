package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.DriverAnalytics;
import com.smartlogistics.analyticsservice.repository.DriverAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DriverReportGenerator implements ReportGeneratorStrategy {

    private final DriverAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "DRIVER";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        List<DriverAnalytics> list;
        if (request.driverId() != null) {
            Optional<DriverAnalytics> da = repository.findByDriverIdAndIsDeletedFalse(request.driverId());
            list = da.isPresent() ? List.of(da.get()) : List.of();
        } else {
            list = repository.findAll();
        }

        double totalRevenue = 0.0;
        double overallRatingSum = 0.0;
        int completedTrips = 0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (DriverAnalytics da : list) {
            totalRevenue += da.getTotalRevenue();
            overallRatingSum += da.getAverageRating();
            completedTrips += da.getCompletedTrips();

            Map<String, Object> row = new HashMap<>();
            row.put("driverId", da.getDriverId());
            row.put("totalTrips", da.getTotalTrips());
            row.put("completedTrips", da.getCompletedTrips());
            row.put("averageRating", da.getAverageRating());
            row.put("revenue", da.getTotalRevenue());
            row.put("trustScore", da.getTrustScore());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDriversTracked", list.size());
        summary.put("aggregatedRevenue", totalRevenue);
        summary.put("totalCompletedTrips", completedTrips);
        summary.put("averageRatingOfDrivers", list.isEmpty() ? 5.0 : overallRatingSum / list.size());

        StringBuilder csv = new StringBuilder("DriverId,TotalTrips,CompletedTrips,AverageRating,Revenue,TrustScore\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("driverId")).append(",")
               .append(r.get("totalTrips")).append(",")
               .append(r.get("completedTrips")).append(",")
               .append(r.get("averageRating")).append(",")
               .append(r.get("revenue")).append(",")
               .append(r.get("trustScore")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("DRIVER", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
