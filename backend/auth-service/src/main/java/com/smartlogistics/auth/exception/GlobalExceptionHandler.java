package com.smartlogistics.auth.exception;

import com.smartlogistics.auth.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
                
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(errors, "VALIDATION_ERROR", request.getRequestURI()));
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(
            RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage(), "UNAUTHORIZED", request.getRequestURI()));
    }

    @ExceptionHandler({AccountLockedException.class, AccountDisabledException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleForbidden(
            RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), "FORBIDDEN", request.getRequestURI()));
    }

    @ExceptionHandler({TokenExpiredException.class, OtpExpiredException.class})
    public ResponseEntity<ApiResponse<Void>> handleExpired(
            RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), "EXPIRED", request.getRequestURI()));
    }
    
    @ExceptionHandler({EmailNotVerifiedException.class, OtpInvalidException.class, ResourceNotFoundException.class, RuntimeException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), "BAD_REQUEST", request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(
            Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred: " + ex.getMessage(), "INTERNAL_SERVER_ERROR", request.getRequestURI()));
    }
}
