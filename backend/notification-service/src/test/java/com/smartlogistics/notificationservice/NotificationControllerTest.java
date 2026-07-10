package com.smartlogistics.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.notificationservice.config.SecurityConfig;
import com.smartlogistics.notificationservice.controller.NotificationController;
import com.smartlogistics.notificationservice.dto.NotificationDto;
import com.smartlogistics.notificationservice.service.NotificationService;
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

@WebMvcTest(NotificationController.class)
@Import(SecurityConfig.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private UUID userId;
    private UUID notificationId;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        notificationId = UUID.randomUUID();
    }

    @Test
    public void searchNotifications_NoHeaders_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void searchNotifications_WithUserHeader_ReturnsOk() throws Exception {
        NotificationDto.Response mockResponse = new NotificationDto.Response(
                notificationId, userId, "SYSTEM", "EMAIL", "Title", "Message",
                "SENT", false, null, null, 0, LocalDateTime.now(), LocalDateTime.now()
        );
        Page<NotificationDto.Response> page = new PageImpl<>(List.of(mockResponse));

        when(notificationService.searchNotifications(
                eq(userId), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/v1/notifications")
                        .header("X-User-Id", userId.toString())
                        .header("X-User-Roles", "DRIVER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(notificationId.toString()));
    }
}
