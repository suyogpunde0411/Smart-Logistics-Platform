package com.smartlogistics.reviewservice;

import com.smartlogistics.reviewservice.config.SecurityConfig;
import com.smartlogistics.reviewservice.controller.ReviewController;
import com.smartlogistics.reviewservice.dto.ReviewDto;
import com.smartlogistics.reviewservice.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@Import(SecurityConfig.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private UUID userId;
    private UUID reviewId;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        reviewId = UUID.randomUUID();
    }

    @Test
    public void searchReviews_NoHeaders_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/reviews"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void searchReviews_WithUserHeaders_ReturnsOk() throws Exception {
        ReviewDto.ReviewRatingDto ratingDto = new ReviewDto.ReviewRatingDto(5, 5, 5, 5, 5, 5, 5);
        ReviewDto.ReviewResponse mockResponse = new ReviewDto.ReviewResponse(
                reviewId, UUID.randomUUID(), userId, UUID.randomUUID(), "DRIVER", "Good driver",
                "APPROVED", ratingDto, List.of(), LocalDateTime.now(), LocalDateTime.now()
        );
        Page<ReviewDto.ReviewResponse> page = new PageImpl<>(List.of(mockResponse));

        when(reviewService.searchReviews(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/v1/reviews")
                        .header("X-User-Id", userId.toString())
                        .header("X-User-Roles", "DRIVER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reviewId.toString()));
    }
}
