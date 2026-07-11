package com.smartlogistics.adminservice.controller;

import com.smartlogistics.adminservice.dto.AuditLogDto;
import com.smartlogistics.adminservice.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
@Tag(name = "Audit Logs", description = "Audit Logs APIs")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @Operation(summary = "Get All Audit Logs")
    public ResponseEntity<List<AuditLogDto>> getAllAuditLogs() {
        return ResponseEntity.ok(auditService.getAllAuditLogs());
    }
}
