package com.smartlogistics.shared.dto;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        LocalDateTime timestamp,
        int status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), 200, message, data);
    }
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), 201, message, data);
    }
}
