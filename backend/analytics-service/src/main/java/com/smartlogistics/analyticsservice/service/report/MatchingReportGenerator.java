package com.smartlogistics.analyticsservice.service.report;

import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.MatchingAnalytics;
import com.smartlogistics.analyticsservice.repository.MatchingAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MatchingReportGenerator implements ReportGeneratorStrategy {

    private final MatchingAnalyticsRepository repository;

    @Override
    public String getReportType() {
        return "MATCHING";
    }

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        Optional<MatchingAnalytics> optional = repository.findLatestAndIsDeletedFalse();
        MatchingAnalytics ma = optional.orElseGet(() -> MatchingAnalytics.builder()
                .totalMatchesCreated(0)
                .totalMatchesAccepted(0)
                .successRate(0.0)
                .build());

        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        row.put("totalMatchesCreated", ma.getTotalMatchesCreated());
        row.put("totalMatchesAccepted", ma.getTotalMatchesAccepted());
        row.put("successRate", ma.getSuccessRate());
        rows.add(row);

        Map<String, Object> summary = new HashMap<>();
        summary.put("matchingSuccessRate", ma.getSuccessRate());
        summary.put("matchesCreated", ma.getTotalMatchesCreated());
        summary.put("matchesAccepted", ma.getTotalMatchesAccepted());

        StringBuilder csv = new StringBuilder("TotalMatchesCreated,TotalMatchesAccepted,SuccessRate\n");
        csv.append(ma.getTotalMatchesCreated()).append(",")
           .append(ma.getTotalMatchesAccepted()).append(",")
           .append(ma.getSuccessRate()).append("\n");
        byte[] exportBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        return new ReportResponseDto("MATCHING", LocalDateTime.now(), summary, rows, exportBytes);
    }
}
