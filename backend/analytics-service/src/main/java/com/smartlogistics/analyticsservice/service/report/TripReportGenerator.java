package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.TripAnalytics;
import com.smartlogistics.analyticsservice.repository.TripAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TripReportGenerator implements ReportGeneratorStrategy {

    private final TripAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "TRIP";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        LocalDateTime start = request.startDate() != null ? request.startDate() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = request.endDate() != null ? request.endDate() : LocalDateTime.now();

        // Search with broad page limits for report downloads
        List<TripAnalytics> trips = repository.searchTrips(
                request.driverId(), request.businessId(), request.truckId(),
                null, null, start, end, Pageable.unpaged()
        ).getContent();

        double totalDistance = 0.0;
        double totalDuration = 0.0;
        int completedTrips = 0;
        int cancelledTrips = 0;
        int emptyTrips = 0;
        int lateTrips = 0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (TripAnalytics ta : trips) {
            if ("COMPLETED".equalsIgnoreCase(ta.getStatus())) {
                completedTrips++;
                totalDistance += ta.getDistanceKm();
                totalDuration += ta.getDurationMinutes();
            } else if ("CANCELLED".equalsIgnoreCase(ta.getStatus())) {
                cancelledTrips++;
            }
            if (Boolean.TRUE.equals(ta.getIsEmpty())) emptyTrips++;
            if (Boolean.TRUE.equals(ta.getIsLate())) lateTrips++;

            Map<String, Object> row = new HashMap<>();
            row.put("tripId", ta.getTripId());
            row.put("driverId", ta.getDriverId());
            row.put("status", ta.getStatus());
            row.put("distanceKm", ta.getDistanceKm());
            row.put("durationMinutes", ta.getDurationMinutes());
            row.put("isEmpty", ta.getIsEmpty());
            row.put("isLate", ta.getIsLate());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalTrips", trips.size());
        summary.put("completedTrips", completedTrips);
        summary.put("cancelledTrips", cancelledTrips);
        summary.put("emptyTripsCount", emptyTrips);
        summary.put("lateDeliveries", lateTrips);
        summary.put("totalDistanceKm", totalDistance);
        summary.put("averageDistanceKm", completedTrips == 0 ? 0.0 : totalDistance / completedTrips);

        StringBuilder csv = new StringBuilder("TripId,DriverId,Status,DistanceKm,DurationMinutes,IsEmpty,IsLate\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("tripId")).append(",")
               .append(r.get("driverId")).append(",")
               .append(r.get("status")).append(",")
               .append(r.get("distanceKm")).append(",")
               .append(r.get("durationMinutes")).append(",")
               .append(r.get("isEmpty")).append(",")
               .append(r.get("isLate")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("TRIP", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
