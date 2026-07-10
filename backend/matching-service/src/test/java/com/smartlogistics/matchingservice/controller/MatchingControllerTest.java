package com.smartlogistics.matchingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.matchingservice.config.SecurityConfig;
import com.smartlogistics.matchingservice.dto.*;
import com.smartlogistics.matchingservice.service.BidService;
import com.smartlogistics.matchingservice.service.MatchingService;
import com.smartlogistics.matchingservice.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchingController.class)
@Import(SecurityConfig.class)
class MatchingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private MatchingService matchingService;
    @MockBean private BidService bidService;
    @MockBean private RuleService ruleService;

    private UUID shipmentId;

    @BeforeEach
    void setUp() {
        shipmentId = UUID.randomUUID();
    }

    @Test
    @WithMockUser(roles = "BUSINESS_OWNER")
    void createMatchRequest_Authorized_ReturnsCreated() throws Exception {
        // Arrange
        MatchRequestDto.CreateRequest request = new MatchRequestDto.CreateRequest(shipmentId, 100.0, 150.0, 120);
        MatchRequestDto.Response response = new MatchRequestDto.Response(
                UUID.randomUUID(), shipmentId, UUID.randomUUID(), "PENDING",
                12.9, 77.5, 13.0, 80.2, 100.0, 150.0,
                LocalDateTime.now().plusHours(2), LocalDateTime.now(), LocalDateTime.now()
        );
        when(matchingService.createMatchRequest(any(MatchRequestDto.CreateRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/matches/requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Match request created and matching executed successfully."))
                .andExpect(jsonPath("$.data.shipmentId").value(shipmentId.toString()));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void createMatchRequest_DriverRole_ReturnsForbidden() throws Exception {
        // Arrange
        MatchRequestDto.CreateRequest request = new MatchRequestDto.CreateRequest(shipmentId, 100.0, 150.0, 120);

        // Act & Assert
        mockMvc.perform(post("/api/v1/matches/requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    void placeBid_Authorized_ReturnsCreated() throws Exception {
        // Arrange
        UUID matchResultId = UUID.randomUUID();
        BidDto.CreateRequest request = new BidDto.CreateRequest(matchResultId, 12000.0, "INR", "Ready", 60);
        BidDto.Response response = new BidDto.Response(
                UUID.randomUUID(), matchResultId, shipmentId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                12000.0, "INR", "PENDING", "Ready", LocalDateTime.now().plusHours(1), LocalDateTime.now(), LocalDateTime.now()
        );
        when(bidService.placeBid(any(BidDto.CreateRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/matches/bids")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.amount").value(12000.0));
    }

    @Test
    @WithMockUser(roles = "BUSINESS_OWNER")
    void getRecommendedTrucks_Authorized_ReturnsList() throws Exception {
        // Arrange
        MatchResultDto.Response res = new MatchResultDto.Response(
                UUID.randomUUID(), UUID.randomUUID(), shipmentId, UUID.randomUUID(), UUID.randomUUID(),
                90.0, 95.0, 90.0, 100.0, 90.0, 45.0, 11000.0,
                "Reasoning", "RECOMMENDED", LocalDateTime.now().plusHours(2), LocalDateTime.now(), LocalDateTime.now()
        );
        when(matchingService.getRecommendedTrucks(shipmentId)).thenReturn(List.of(res));

        // Act & Assert
        mockMvc.perform(get("/api/v1/matches/recommended-trucks/{shipmentId}", shipmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].overallScore").value(90.0));
    }
}
