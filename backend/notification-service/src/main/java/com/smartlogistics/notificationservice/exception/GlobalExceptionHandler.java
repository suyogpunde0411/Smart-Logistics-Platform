package com.smartlogistics.notificationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificationNotFound(NotificationNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "NOTIFICATION_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTemplateNotFound(TemplateNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "TEMPLATE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(PreferenceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePreferenceNotFound(PreferenceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "PREFERENCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(DeliveryFailedException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryFailed(DeliveryFailedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "DELIVERY_FAILED", ex.getMessage());
    }

    @ExceptionHandler(InvalidChannelException.class)
    public ResponseEntity<ErrorResponse> handleInvalidChannel(InvalidChannelException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_CHANNEL", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You do not have permission to perform this action");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "VALIDATION_FAILED");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        body.put("details", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(LocalDateTime.now(), status.value(), error, message));
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message
    ) {}
}
