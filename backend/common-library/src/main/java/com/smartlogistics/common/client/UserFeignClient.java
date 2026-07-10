package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", path = "/internal/users")
public interface UserFeignClient {

    @GetMapping("/{id}")
    InternalUserResponse getUser(@PathVariable("id") UUID id);

    @GetMapping("/{id}/driver/status")
    DriverStatusResponse getDriverStatus(@PathVariable("id") UUID id);

    record InternalUserResponse(
            UUID id,
            String email,
            String phone,
            String firstName,
            String lastName,
            String status
    ) {}

    record DriverStatusResponse(
            UUID userId,
            String licenseNumber,
            String status, // PENDING, VERIFIED, REJECTED
            boolean active
    ) {}
}
