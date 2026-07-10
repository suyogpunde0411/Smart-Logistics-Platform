package com.smartlogistics.trackingservice.exception;

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

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTripNotFound(TripNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "TRIP_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(TripAlreadyStartedException.class)
    public ResponseEntity<ErrorResponse> handleTripAlreadyStarted(TripAlreadyStartedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "TRIP_ALREADY_STARTED", ex.getMessage());
    }

    @ExceptionHandler(TripAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleTripAlreadyCompleted(TripAlreadyCompletedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "TRIP_ALREADY_COMPLETED", ex.getMessage());
    }

    @ExceptionHandler(InvalidTripStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTripState(InvalidTripStateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_TRIP_STATE", ex.getMessage());
    }

    @ExceptionHandler(GpsValidationException.class)
    public ResponseEntity<ErrorResponse> handleGpsValidation(GpsValidationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "GPS_VALIDATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(CheckpointException.class)
    public ResponseEntity<ErrorResponse> handleCheckpoint(CheckpointException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "CHECKPOINT_ERROR", ex.getMessage());
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
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
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
