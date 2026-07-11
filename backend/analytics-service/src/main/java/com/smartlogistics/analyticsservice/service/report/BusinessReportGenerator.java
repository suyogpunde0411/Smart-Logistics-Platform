package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.BusinessAnalytics;
import com.smartlogistics.analyticsservice.repository.BusinessAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class BusinessReportGenerator implements ReportGeneratorStrategy {

    private final BusinessAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "BUSINESS";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        List<BusinessAnalytics> list;
        if (request.businessId() != null) {
            Optional<BusinessAnalytics> ba = repository.findByBusinessIdAndIsDeletedFalse(request.businessId());
            list = ba.isPresent() ? List.of(ba.get()) : List.of();
        } else {
            list = repository.findAll();
        }

        double totalBudgetSpent = 0.0;
        int completedShipments = 0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (BusinessAnalytics ba : list) {
            totalBudgetSpent += ba.getTotalBudgetSpent();
            completedShipments += ba.getCompletedShipments();

            Map<String, Object> row = new HashMap<>();
            row.put("businessId", ba.getBusinessId());
            row.put("totalShipments", ba.getTotalShipments());
            row.put("completedShipments", ba.getCompletedShipments());
            row.put("cancelledShipments", ba.getCancelledShipments());
            row.put("budgetSpent", ba.getTotalBudgetSpent());
            row.put("averageRating", ba.getAverageRating());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalBusinessesTracked", list.size());
        summary.put("aggregatedSpent", totalBudgetSpent);
        summary.put("totalCompletedShipments", completedShipments);

        StringBuilder csv = new StringBuilder("BusinessId,TotalShipments,CompletedShipments,CancelledShipments,BudgetSpent,AverageRating\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("businessId")).append(",")
               .append(r.get("totalShipments")).append(",")
               .append(r.get("completedShipments")).append(",")
               .append(r.get("cancelledShipments")).append(",")
               .append(r.get("budgetSpent")).append(",")
               .append(r.get("averageRating")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("BUSINESS", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
