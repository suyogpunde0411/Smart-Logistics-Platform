package com.smartlogistics.reviewservice.controller;

import com.smartlogistics.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/reviews")
@RequiredArgsConstructor
public class InternalReviewController {

    private final ReviewService reviewService;

    @GetMapping("/average-rating/{userId}")
    public Double getAverageRating(@PathVariable UUID userId) {
        return reviewService.getAverageRating(userId);
    }
}
