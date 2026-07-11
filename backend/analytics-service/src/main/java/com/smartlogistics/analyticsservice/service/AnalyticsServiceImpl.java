package com.smartlogistics.analyticsservice.service;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.dto.TripAnalyticsResponseDto;
import com.smartlogistics.analyticsservice.entity.DashboardMetric;
import com.smartlogistics.analyticsservice.entity.TripAnalytics;
import com.smartlogistics.analyticsservice.mapper.AnalyticsMapper;
import com.smartlogistics.analyticsservice.repository.DashboardMetricRepository;
import com.smartlogistics.analyticsservice.repository.TripAnalyticsRepository;
import com.smartlogistics.analyticsservice.service.dashboard.DashboardBuilderFactory;
import com.smartlogistics.analyticsservice.service.report.ReportGeneratorRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final DashboardBuilderFactory builderFactory;
    private final ReportGeneratorRegistry reportRegistry;
    private final TripAnalyticsRepository tripRepository;
    private final DashboardMetricRepository metricRepository;
    private final AnalyticsMapper mapper;

    @Override
    @Cacheable(value = "dashboards", key = "#role + '-' + (#entityId != null ? #entityId.toString() : 'all')")
    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboard(String role, UUID entityId) {
        log.info("Generating dashboard summary metrics for role: {}, entity: {}", role, entityId);
        return builderFactory.getBuilder(role)
                .buildSummaryMetrics(entityId)
                .buildChartData(entityId)
                .buildSecondaryKpis(entityId)
                .getResult();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponseDto generateReport(ReportRequestDto request) {
        log.info("Strategic generation triggered for report type: {}", request.reportType());
        return reportRegistry.getStrategy(request.reportType())
                .generateReport(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TripAnalyticsResponseDto> searchTrips(
            UUID driverId, UUID businessId, UUID truckId, String status, String city,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Searching trip analytics history");
        Page<TripAnalytics> page = tripRepository.searchTrips(
                driverId, businessId, truckId, status, city, startDate, endDate, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTopRoutes() {
        log.info("Calculating top route statistics");
        List<Object[]> queryResults = tripRepository.findTopRoutes();
        List<Map<String, Object>> results = new ArrayList<>();

        for (Object[] arr : queryResults.subList(0, Math.min(queryResults.size(), 10))) {
            Map<String, Object> map = new HashMap<>();
            map.put("origin", arr[0]);
            map.put("destination", arr[1]);
            map.put("tripCount", arr[2]);
            results.add(map);
        }
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getKpis() {
        log.info("Fetching current KPI snapshot states");
        List<DashboardMetric> metrics = metricRepository.findAll();
        Map<String, Object> map = new HashMap<>();
        for (DashboardMetric dm : metrics) {
            map.put(dm.getMetricKey(), dm.getMetricValue());
        }
        return map;
    }
}
