package com.smartlogistics.trackingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.trackingservice.config.SecurityConfig;
import com.smartlogistics.trackingservice.dto.TripDto;
import com.smartlogistics.trackingservice.dto.TripRouteDto;
import com.smartlogistics.trackingservice.service.TripService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
@Import(SecurityConfig.class)
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TripService tripService;

    private UUID tripId;
    private UUID shipmentId;

    @BeforeEach
    public void setUp() {
        tripId = UUID.randomUUID();
        shipmentId = UUID.randomUUID();
    }

    @Test
    @WithMockUser(roles = "BUSINESS_OWNER")
    public void manualCreateTrip_Authorized_ReturnsCreated() throws Exception {
        TripRouteDto.CreateRequest routeRequest = new TripRouteDto.CreateRequest(
                "Mumbai", 19.076, 72.877, "Delhi", 28.613, 77.209, 1400.0, 24.0
        );
        TripDto.CreateRequest createRequest = new TripDto.CreateRequest(
                shipmentId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), routeRequest, null
        );

        TripDto.Response mockResponse = new TripDto.Response(
                tripId, shipmentId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "ASSIGNED", null, null, 0.0, null, null,
                null, null, 1400.0, 50.0, null, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(tripService.createTripManual(any(TripDto.CreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/trips")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void manualCreateTrip_Unauthorized_ReturnsForbidden() throws Exception {
        TripRouteDto.CreateRequest routeRequest = new TripRouteDto.CreateRequest(
                "Mumbai", 19.076, 72.877, "Delhi", 28.613, 77.209, 1400.0, 24.0
        );
        TripDto.CreateRequest createRequest = new TripDto.CreateRequest(
                shipmentId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), routeRequest, null
        );

        mockMvc.perform(post("/api/v1/trips")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void getTripById_Authorized_ReturnsOk() throws Exception {
        TripDto.Response mockResponse = new TripDto.Response(
                tripId, shipmentId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "ASSIGNED", null, null, 0.0, null, null,
                null, null, 1400.0, 50.0, null, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(tripService.getTripById(tripId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/trips/{id}", tripId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()));
    }
}
