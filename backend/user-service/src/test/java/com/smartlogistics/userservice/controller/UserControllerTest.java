package com.smartlogistics.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.userservice.dto.AddressDto.AddressRequest;
import com.smartlogistics.userservice.dto.AddressDto.AddressResponse;
import com.smartlogistics.userservice.dto.PreferenceDto.PreferenceResponse;
import com.smartlogistics.shared.dto.UserDTO.UserResponse;
import com.smartlogistics.shared.dto.UserDTO.UserUpdateRequest;
import com.smartlogistics.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse mockResponse = new UserResponse(
                userId, "test@example.com", "+1234567890", "John", "Doe", "ACTIVE"
        );

        Mockito.when(userService.getUser(userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(userId.toString()))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenValidRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest("Jane", "Doe", "+19876543210");
        
        UserResponse mockResponse = new UserResponse(
                userId, "test@example.com", "+19876543210", "Jane", "Doe", "ACTIVE"
        );

        Mockito.when(userService.updateUser(eq(userId), any(UserUpdateRequest.class)))
               .thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Jane"))
                .andExpect(jsonPath("$.data.phone").value("+19876543210"));
    }
    
    @Test
    void updateUser_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        UUID userId = UUID.randomUUID();
        UserUpdateRequest invalidRequest = new UserUpdateRequest("", "Doe", "+19876543210");

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPreference_ShouldReturnPreferences() throws Exception {
        UUID userId = UUID.randomUUID();
        PreferenceResponse mockResponse = new PreferenceResponse("en", "DARK");

        Mockito.when(userService.getPreference(userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/users/{id}/preferences", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.language").value("en"))
                .andExpect(jsonPath("$.data.theme").value("DARK"));
    }

    @Test
    void addAddress_ShouldReturnAddressResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        AddressRequest request = new AddressRequest("Line 1", "Line 2", "Mumbai", "MH", "400001", "India", "HOME");
        AddressResponse response = new AddressResponse(UUID.randomUUID(), "Line 1", "Line 2", "Mumbai", "MH", "400001", "India", "HOME");

        Mockito.when(userService.addAddress(eq(userId), any(AddressRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/users/{id}/addresses", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.city").value("Mumbai"))
                .andExpect(jsonPath("$.data.zip").value("400001"));
    }

    @Test
    void deleteAddress_ShouldReturnSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/users/{id}/addresses/{addressId}", userId, addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Address deleted successfully"));

        Mockito.verify(userService, Mockito.times(1)).deleteAddress(userId, addressId);
    }
}
