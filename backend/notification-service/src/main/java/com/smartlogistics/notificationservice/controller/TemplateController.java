package com.smartlogistics.notificationservice.controller;

import com.smartlogistics.notificationservice.dto.TemplateDto;
import com.smartlogistics.notificationservice.service.NotificationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications/templates")
@RequiredArgsConstructor
@Tag(name = "Template Management", description = "Endpoints for managing notification message templates")
public class TemplateController {

    private final NotificationTemplateService templateService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new notification template (Admin Only)")
    public ResponseEntity<TemplateDto.Response> createTemplate(@Valid @RequestBody TemplateDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateService.createTemplate(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get a template by ID")
    public ResponseEntity<TemplateDto.Response> getTemplateById(@PathVariable UUID id) {
        return ResponseEntity.ok(templateService.getTemplateById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DRIVER', 'BUSINESS_OWNER', 'FLEET_OWNER', 'ADMIN')")
    @Operation(summary = "Get list of all notification templates")
    public ResponseEntity<List<TemplateDto.Response>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a template (Admin Only)")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
