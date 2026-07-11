package com.smartlogistics.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserVerifyClient {

    @PutMapping("/api/v1/users/{id}/driver-profile/verify")
    void verifyDriverProfile(@PathVariable("id") UUID id, @RequestParam("status") String status);

    @PutMapping("/api/v1/users/{id}/business-profile/verify")
    void verifyBusinessProfile(@PathVariable("id") UUID id, @RequestParam("status") String status);
}
