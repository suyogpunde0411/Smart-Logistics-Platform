package com.smartlogistics.userservice.dto;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String email,
        String phone,
        String role
) {}
