package com.smartlogistics.truckservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.service.TruckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TruckController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TruckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TruckService truckService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterTruck() throws Exception {
        UUID ownerId = UUID.randomUUID();
        TruckDTO.RegisterRequest request = new TruckDTO.RegisterRequest("MH12AB1234", ownerId, 1000.0, 10.0);

        TruckDTO.Response response = new TruckDTO.Response(
                UUID.randomUUID(),
                "MH12AB1234",
                ownerId,
                "ACTIVE",
                new TruckDTO.CapacityDto(1000.0, 10.0),
                new TruckDTO.AvailabilityDto("AVAILABLE", true),
                new TruckDTO.LocationDto(0.0, 0.0, null, null, null, null),
                null,
                null,
                null
        );

        when(truckService.registerTruck(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/trucks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationNumber").value("MH12AB1234"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    public void testGetTruck() throws Exception {
        UUID truckId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        TruckDTO.Response response = new TruckDTO.Response(
                truckId,
                "MH12AB1234",
                ownerId,
                "ACTIVE",
                new TruckDTO.CapacityDto(1000.0, 10.0),
                new TruckDTO.AvailabilityDto("AVAILABLE", true),
                new TruckDTO.LocationDto(0.0, 0.0, null, null, null, null),
                null,
                null,
                null
        );

        when(truckService.getTruck(truckId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/trucks/{id}", truckId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(truckId.toString()))
                .andExpect(jsonPath("$.registrationNumber").value("MH12AB1234"));
    }
}
