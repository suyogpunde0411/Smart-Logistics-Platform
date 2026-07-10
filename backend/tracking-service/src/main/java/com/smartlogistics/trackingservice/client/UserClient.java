package com.smartlogistics.trackingservice.client;

import com.smartlogistics.trackingservice.config.TrackingFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", contextId = "trackingUserClient", configuration = TrackingFeignConfig.class)
public interface UserClient {

    @GetMapping("/internal/users/{id}")
    InternalUserResponse getUser(@PathVariable("id") UUID id);

    @GetMapping("/internal/users/{id}/driver/status")
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
            String status,
            boolean active
    ) {}
}
