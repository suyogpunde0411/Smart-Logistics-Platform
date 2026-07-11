package com.smartlogistics.adminservice.controller;

import com.smartlogistics.adminservice.entity.BlockedEntity;
import com.smartlogistics.adminservice.entity.ModerationRecord;
import com.smartlogistics.adminservice.service.ModerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/moderation")
@Tag(name = "Moderation", description = "Moderation APIs")
public class ModerationController {

    private final ModerationService moderationService;

    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PostMapping("/reviews/{reviewId}/remove")
    @Operation(summary = "Remove a Review")
    public ResponseEntity<Void> removeReview(
            @PathVariable UUID reviewId,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String reason) {
        moderationService.removeReview(reviewId, adminId, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/suspend")
    @Operation(summary = "Suspend a User")
    public ResponseEntity<Void> suspendUser(
            @PathVariable UUID userId,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String reason) {
        moderationService.suspendUser(userId, adminId, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/entities/{entityId}/unblock")
    @Operation(summary = "Unblock an Entity")
    public ResponseEntity<Void> unblockEntity(
            @PathVariable UUID entityId,
            @RequestHeader("X-User-Id") UUID adminId,
            @RequestParam String reason) {
        moderationService.unblockEntity(entityId, adminId, reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/records")
    @Operation(summary = "Get all moderation records")
    public ResponseEntity<List<ModerationRecord>> getModerationRecords() {
        return ResponseEntity.ok(moderationService.getAllModerationRecords());
    }

    @GetMapping("/blocked-entities")
    @Operation(summary = "Get all blocked entities")
    public ResponseEntity<List<BlockedEntity>> getBlockedEntities() {
        return ResponseEntity.ok(moderationService.getAllBlockedEntities());
    }
}
