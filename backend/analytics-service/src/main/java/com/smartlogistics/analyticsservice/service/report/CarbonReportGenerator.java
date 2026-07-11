package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.CarbonSavings;
import com.smartlogistics.analyticsservice.repository.CarbonSavingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CarbonReportGenerator implements ReportGeneratorStrategy {

    private final CarbonSavingsRepository repository;

    @Override
    public String getReportType() {
        return "CARBON";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        List<CarbonSavings> list;
        if (request.driverId() != null) {
            Optional<CarbonSavings> cs = repository.findByEntityIdAndEntityTypeAndIsDeletedFalse(request.driverId(), "DRIVER");
            list = cs.isPresent() ? List.of(cs.get()) : List.of();
        } else if (request.truckId() != null) {
            Optional<CarbonSavings> cs = repository.findByEntityIdAndEntityTypeAndIsDeletedFalse(request.truckId(), "TRUCK");
            list = cs.isPresent() ? List.of(cs.get()) : List.of();
        } else if (request.fleetOwnerId() != null) {
            Optional<CarbonSavings> cs = repository.findByEntityIdAndEntityTypeAndIsDeletedFalse(request.fleetOwnerId(), "FLEET");
            list = cs.isPresent() ? List.of(cs.get()) : List.of();
        } else {
            list = repository.findAll();
        }

        double totalCo2Saved = 0.0;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (CarbonSavings cs : list) {
            totalCo2Saved += cs.getCo2SavedKg();

            Map<String, Object> row = new HashMap<>();
            row.put("entityId", cs.getEntityId());
            row.put("entityType", cs.getEntityType());
            row.put("co2SavedKg", cs.getCo2SavedKg());
            rows.add(row);
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCo2SavingsKg", totalCo2Saved);
        summary.put("totalEntitiesCount", list.size());
        summary.put("averageCo2SavingsPerEntity", list.isEmpty() ? 0.0 : totalCo2Saved / list.size());

        StringBuilder csv = new StringBuilder("EntityId,EntityType,Co2SavedKg\n");
        for (Map<String, Object> r : rows) {
            csv.append(r.get("entityId")).append(",")
               .append(r.get("entityType")).append(",")
               .append(r.get("co2SavedKg")).append("\n");
        }
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("CARBON", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
