package com.smartlogistics.adminservice.controller;

import com.smartlogistics.adminservice.dto.AdminDashboardResponseDto;
import com.smartlogistics.adminservice.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "Admin Dashboard APIs")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(summary = "Get Dashboard Stats")
    public ResponseEntity<AdminDashboardResponseDto> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
}
