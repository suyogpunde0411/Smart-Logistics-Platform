package com.smartlogistics.truckservice.exception;

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

    @ExceptionHandler(TruckNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTruckNotFound(TruckNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "TRUCK_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(DuplicateRegistrationNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRegNum(DuplicateRegistrationNumberException ex) {
        return buildResponse(HttpStatus.CONFLICT, "DUPLICATE_REGISTRATION", ex.getMessage());
    }

    @ExceptionHandler(InvalidCapacityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCapacity(InvalidCapacityException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_CAPACITY", ex.getMessage());
    }

    @ExceptionHandler(InsuranceExpiredException.class)
    public ResponseEntity<ErrorResponse> handleInsuranceExpired(InsuranceExpiredException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INSURANCE_EXPIRED", ex.getMessage());
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDocumentNotFound(DocumentNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "DOCUMENT_NOT_FOUND", ex.getMessage());
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

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), error, message);
        return ResponseEntity.status(status).body(response);
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message
    ) {}
}
