package com.smartlogistics.shared.util;

import com.smartlogistics.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {
    public static <T> ResponseEntity<ApiResponse<T>> buildSuccess(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildCreated(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(message, data));
    }
}
