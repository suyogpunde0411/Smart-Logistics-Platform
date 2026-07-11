package com.smartlogistics.adminservice.controller;

import com.smartlogistics.adminservice.dto.SupportTicketDto;
import com.smartlogistics.adminservice.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/support-tickets")
@Tag(name = "Support Tickets", description = "Support Tickets APIs")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping
    @Operation(summary = "Create Support Ticket")
    public ResponseEntity<SupportTicketDto> createTicket(@RequestBody SupportTicketDto dto) {
        return ResponseEntity.ok(supportTicketService.createTicket(dto));
    }

    @GetMapping
    @Operation(summary = "Get All Support Tickets")
    public ResponseEntity<List<SupportTicketDto>> getAllTickets() {
        return ResponseEntity.ok(supportTicketService.getAllTickets());
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve Support Ticket")
    public ResponseEntity<SupportTicketDto> resolveTicket(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String resolution) {
        return ResponseEntity.ok(supportTicketService.resolveTicket(id, adminId, resolution));
    }
}
