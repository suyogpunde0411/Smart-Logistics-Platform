package com.smartlogistics.matchingservice.controller;

import com.smartlogistics.matchingservice.dto.*;
import com.smartlogistics.matchingservice.service.BidService;
import com.smartlogistics.matchingservice.service.MatchingService;
import com.smartlogistics.matchingservice.service.RuleService;
import com.smartlogistics.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Matching & Bidding", description = "Endpoints for Shipment-Truck Matching, Driver Bidding, and Scoring Rules")
public class MatchingController {

    private final MatchingService matchingService;
    private final BidService bidService;
    private final RuleService ruleService;

    @PostMapping("/requests")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Create a manual match request and trigger matching engine")
    public ResponseEntity<ApiResponse<MatchRequestDto.Response>> createMatchRequest(
            @Valid @RequestBody MatchRequestDto.CreateRequest request) {
        log.info("Received request to create MatchRequest for shipment: {}", request.shipmentId());
        MatchRequestDto.Response response = matchingService.createMatchRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Match request created and matching executed successfully.", response));
    }

    @PostMapping("/requests/{shipmentId}/match")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Manually execute the matching engine for a shipment")
    public ResponseEntity<ApiResponse<List<MatchResultDto.Response>>> runManualMatching(
            @PathVariable UUID shipmentId) {
        log.info("Received request to manually match shipment: {}", shipmentId);
        List<MatchResultDto.Response> results = matchingService.runManualMatching(shipmentId);
        return ResponseEntity.ok(ApiResponse.success("Manual matching executed successfully.", results));
    }

    @PostMapping("/bids")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Driver places a bid on a recommended match result")
    public ResponseEntity<ApiResponse<BidDto.Response>> placeBid(
            @Valid @RequestBody BidDto.CreateRequest request) {
        log.info("Received request to place bid on match: {}", request.matchResultId());
        BidDto.Response response = bidService.placeBid(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Bid placed successfully.", response));
    }

    @PostMapping("/bids/{id}/accept")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Business owner accepts a driver's bid")
    public ResponseEntity<ApiResponse<BidDto.Response>> acceptBid(
            @PathVariable UUID id) {
        log.info("Received request to accept bid: {}", id);
        BidDto.Response response = bidService.acceptBid(id);
        return ResponseEntity.ok(ApiResponse.success("Bid accepted successfully.", response));
    }

    @PostMapping("/bids/{id}/reject")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Business owner rejects a driver's bid")
    public ResponseEntity<ApiResponse<BidDto.Response>> rejectBid(
            @PathVariable UUID id,
            @Valid @RequestBody BidDto.RejectRequest request) {
        log.info("Received request to reject bid: {}", id);
        BidDto.Response response = bidService.rejectBid(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bid rejected successfully.", response));
    }

    @GetMapping("/recommended-trucks/{shipmentId}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    @Operation(summary = "Get top matching trucks for a shipment")
    public ResponseEntity<ApiResponse<List<MatchResultDto.Response>>> getRecommendedTrucks(
            @PathVariable UUID shipmentId) {
        log.info("Fetching recommended trucks for shipment: {}", shipmentId);
        List<MatchResultDto.Response> results = matchingService.getRecommendedTrucks(shipmentId);
        return ResponseEntity.ok(ApiResponse.success("Recommended trucks retrieved successfully.", results));
    }

    @GetMapping("/recommended-shipments/{truckId}")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @Operation(summary = "Get top matching shipments for a truck")
    public ResponseEntity<ApiResponse<List<MatchResultDto.Response>>> getRecommendedShipments(
            @PathVariable UUID truckId) {
        log.info("Fetching recommended shipments for truck: {}", truckId);
        List<MatchResultDto.Response> results = matchingService.getRecommendedShipments(truckId);
        return ResponseEntity.ok(ApiResponse.success("Recommended shipments retrieved successfully.", results));
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'DRIVER', 'ADMIN')")
    @Operation(summary = "Retrieve matching history (accepted and rejected matches)")
    public ResponseEntity<ApiResponse<List<MatchHistoryDto>>> getMatchHistory() {
        log.info("Fetching matching history.");
        List<MatchHistoryDto> history = matchingService.getMatchHistory();
        return ResponseEntity.ok(ApiResponse.success("Match history retrieved successfully.", history));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'DRIVER', 'ADMIN')")
    @Operation(summary = "Search, filter, and page match results")
    public ResponseEntity<ApiResponse<Page<MatchResultDto.Response>>> searchMatches(
            @RequestParam(value = "shipmentId", required = false) UUID shipmentId,
            @RequestParam(value = "truckId", required = false) UUID truckId,
            @RequestParam(value = "driverId", required = false) UUID driverId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "minScore", required = false) Double minScore,
            @RequestParam(value = "maxScore", required = false) Double maxScore,
            Pageable pageable) {
        log.info("Searching match results.");
        Page<MatchResultDto.Response> page = matchingService.searchMatches(
                shipmentId, truckId, driverId, status, minScore, maxScore, pageable);
        return ResponseEntity.ok(ApiResponse.success("Match results searched successfully.", page));
    }

    @GetMapping("/rules")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get list of all configurable scoring rules")
    public ResponseEntity<ApiResponse<List<MatchRuleDto.Response>>> getRules() {
        log.info("Fetching all scoring rules.");
        List<MatchRuleDto.Response> rules = ruleService.getAllRules();
        return ResponseEntity.ok(ApiResponse.success("Configured scoring rules retrieved successfully.", rules));
    }

    @PutMapping("/rules/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update weight or active status of a scoring rule")
    public ResponseEntity<ApiResponse<MatchRuleDto.Response>> updateRule(
            @PathVariable String code,
            @Valid @RequestBody MatchRuleDto.UpdateRequest request) {
        log.info("Updating scoring rule: {}", code);
        MatchRuleDto.Response response = ruleService.updateRule(code, request);
        return ResponseEntity.ok(ApiResponse.success("Scoring rule updated successfully.", response));
    }
}
