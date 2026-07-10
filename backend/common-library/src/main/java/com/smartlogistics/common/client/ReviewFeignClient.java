package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "review-service", path = "/internal/reviews")
public interface ReviewFeignClient {

    @GetMapping("/average-rating/{userId}")
    Double getAverageRating(@PathVariable("userId") UUID userId);
}
