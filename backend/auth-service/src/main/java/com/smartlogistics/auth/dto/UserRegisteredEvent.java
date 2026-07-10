package com.smartlogistics.auth.dto;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String email,
        String phone,
        String role
) {}
