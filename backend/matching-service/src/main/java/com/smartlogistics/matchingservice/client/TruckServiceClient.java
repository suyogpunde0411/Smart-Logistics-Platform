package com.smartlogistics.matchingservice.client;

import com.smartlogistics.matchingservice.config.MatchingFeignConfig;
import com.smartlogistics.matchingservice.dto.CustomPage;
import com.smartlogistics.shared.dto.TruckDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "truck-service", contextId = "matchingTruckServiceClient", configuration = MatchingFeignConfig.class)
public interface TruckServiceClient {

    @GetMapping("/api/v1/trucks/{id}")
    TruckDTO.Response getTruckById(@PathVariable("id") UUID id);

    @GetMapping("/api/v1/trucks/nearby")
    CustomPage<TruckDTO.Response> findNearbyTrucks(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("radius") Double radius,
            @RequestParam("page") int page,
            @RequestParam("size") int size);

    @GetMapping("/api/v1/trucks")
    CustomPage<TruckDTO.Response> searchTrucks(
            @RequestParam(value = "regNum", required = false) String regNum,
            @RequestParam(value = "ownerId", required = false) UUID ownerId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "availStatus", required = false) String availStatus,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "minWeight", required = false) Double minWeight,
            @RequestParam(value = "minVolume", required = false) Double minVolume,
            @RequestParam("page") int page,
            @RequestParam("size") int size);
}
