package com.smartlogistics.analyticsservice.service.report;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportGeneratorRegistry {

    private final Map<String, ReportGeneratorStrategy> strategies = new HashMap<>();

    public ReportGeneratorRegistry(List<ReportGeneratorStrategy> strategyList) {
        for (ReportGeneratorStrategy strategy : strategyList) {
            strategies.put(strategy.getReportType().toUpperCase(), strategy);
        }
    }

    public ReportGeneratorStrategy getStrategy(String reportType) {
        if (reportType == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }
        ReportGeneratorStrategy strategy = strategies.get(reportType.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("No report strategy found for type: " + reportType);
        }
        return strategy;
    }
}
