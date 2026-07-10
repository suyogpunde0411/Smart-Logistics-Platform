package com.smartlogistics.shipmentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShipmentNotFound(ShipmentNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "SHIPMENT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidShipmentStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidShipmentStateException ex) {
        return buildResponse(HttpStatus.CONFLICT, "INVALID_SHIPMENT_STATE", ex.getMessage());
    }

    @ExceptionHandler(DuplicateShipmentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateShipmentException ex) {
        return buildResponse(HttpStatus.CONFLICT, "DUPLICATE_SHIPMENT", ex.getMessage());
    }

    @ExceptionHandler(InvalidWeightException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWeight(InvalidWeightException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_WEIGHT", ex.getMessage());
    }

    @ExceptionHandler(InvalidScheduleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSchedule(InvalidScheduleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_SCHEDULE", ex.getMessage());
    }

    @ExceptionHandler(BusinessNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBusinessNotFound(BusinessNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "BUSINESS_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDocumentNotFound(DocumentNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "DOCUMENT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You do not have permission to perform this action");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
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
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred");
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
