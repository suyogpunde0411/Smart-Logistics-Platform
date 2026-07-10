package com.smartlogistics.matchingservice.exception;

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

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMatchNotFound(MatchNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "MATCH_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(BidNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBidNotFound(BidNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "BID_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(DuplicateBidException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateBid(DuplicateBidException ex) {
        return buildResponse(HttpStatus.CONFLICT, "DUPLICATE_BID", ex.getMessage());
    }

    @ExceptionHandler(InvalidMatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMatch(InvalidMatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_MATCH", ex.getMessage());
    }

    @ExceptionHandler(ShipmentUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleShipmentUnavailable(ShipmentUnavailableException ex) {
        return buildResponse(HttpStatus.CONFLICT, "SHIPMENT_UNAVAILABLE", ex.getMessage());
    }

    @ExceptionHandler(TruckUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleTruckUnavailable(TruckUnavailableException ex) {
        return buildResponse(HttpStatus.CONFLICT, "TRUCK_UNAVAILABLE", ex.getMessage());
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
