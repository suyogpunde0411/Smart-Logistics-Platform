package com.smartlogistics.analyticsservice;

import com.smartlogistics.analyticsservice.dto.DashboardResponseDto;
import com.smartlogistics.analyticsservice.dto.ReportRequestDto;
import com.smartlogistics.analyticsservice.dto.ReportResponseDto;
import com.smartlogistics.analyticsservice.entity.DriverAnalytics;
import com.smartlogistics.analyticsservice.entity.MatchingAnalytics;
import com.smartlogistics.analyticsservice.repository.*;
import com.smartlogistics.analyticsservice.service.AnalyticsServiceImpl;
import com.smartlogistics.analyticsservice.service.dashboard.DashboardBuilderFactory;
import com.smartlogistics.analyticsservice.service.report.ReportGeneratorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    private DashboardBuilderFactory builderFactory;

    @Mock
    private ReportGeneratorRegistry reportRegistry;

    @Mock
    private TripAnalyticsRepository tripRepository;

    @Mock
    private DashboardMetricRepository metricRepository;

    @Mock
    private DriverAnalyticsRepository driverAnalyticsRepository;

    @Mock
    private MatchingAnalyticsRepository matchingRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    private UUID entityId;

    @BeforeEach
    public void setup() {
        entityId = UUID.randomUUID();
    }

    @Test
    public void testGetDashboard_ForDriver() {
        com.smartlogistics.analyticsservice.service.dashboard.DriverDashboardBuilder mockBuilder = 
                mock(com.smartlogistics.analyticsservice.service.dashboard.DriverDashboardBuilder.class);
        
        when(builderFactory.getBuilder("DRIVER")).thenReturn(mockBuilder);
        when(mockBuilder.buildSummaryMetrics(entityId)).thenReturn(mockBuilder);
        when(mockBuilder.buildChartData(entityId)).thenReturn(mockBuilder);
        when(mockBuilder.buildSecondaryKpis(entityId)).thenReturn(mockBuilder);
        
        DashboardResponseDto mockResponse = new DashboardResponseDto("DRIVER", java.util.Map.of("totalTrips", 10), java.util.Map.of(), java.util.Map.of());
        when(mockBuilder.getResult()).thenReturn(mockResponse);

        DashboardResponseDto response = analyticsService.getDashboard("DRIVER", entityId);

        assertNotNull(response);
        assertEquals("DRIVER", response.role());
        assertEquals(10, response.summaryMetrics().get("totalTrips"));
    }

    @Test
    public void testGenerateReport_StrategyExecution() {
        ReportRequestDto request = new ReportRequestDto("REVENUE", LocalDateTime.now().minusDays(5), LocalDateTime.now(), null, null, null, null, "CSV");
        com.smartlogistics.analyticsservice.service.report.ReportGeneratorStrategy mockStrategy = 
                mock(com.smartlogistics.analyticsservice.service.report.ReportGeneratorStrategy.class);

        when(reportRegistry.getStrategy("REVENUE")).thenReturn(mockStrategy);
        ReportResponseDto mockResponse = new ReportResponseDto("REVENUE", LocalDateTime.now(), java.util.Map.of("totalRevenue", 5000.0), List.of(), new byte[0]);
        when(mockStrategy.generateReport(request)).thenReturn(mockResponse);

        ReportResponseDto response = analyticsService.generateReport(request);

        assertNotNull(response);
        assertEquals("REVENUE", response.reportType());
        assertEquals(5000.0, response.summaryMetrics().get("totalRevenue"));
    }
}
