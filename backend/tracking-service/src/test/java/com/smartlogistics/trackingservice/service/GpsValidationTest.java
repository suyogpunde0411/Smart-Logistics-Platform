package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.GpsLocationDto;
import com.smartlogistics.trackingservice.exception.GpsValidationException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class GpsValidationTest {

    @Test
    public void testCoordinatesValidation() throws Exception {
        GpsTrackingServiceImpl service = new GpsTrackingServiceImpl(null, null, null, null, null, null);
        Method method = GpsTrackingServiceImpl.class.getDeclaredMethod("validateGpsRequest", GpsLocationDto.CreateRequest.class);
        method.setAccessible(true);

        // Valid
        GpsLocationDto.CreateRequest valid = new GpsLocationDto.CreateRequest(45.0, 90.0, 50.0, 180.0, 5.0, 100.0, LocalDateTime.now());
        assertDoesNotThrow(() -> {
            try {
                method.invoke(service, valid);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
        });

        // Invalid Latitude
        GpsLocationDto.CreateRequest invalidLat = new GpsLocationDto.CreateRequest(95.0, 90.0, 50.0, 180.0, 5.0, 100.0, LocalDateTime.now());
        Exception exLat = assertThrows(Exception.class, () -> method.invoke(service, invalidLat));
        assertTrue(exLat.getCause() instanceof GpsValidationException);
        assertEquals("Latitude must be between -90 and 90 degrees.", exLat.getCause().getMessage());

        // Invalid Longitude
        GpsLocationDto.CreateRequest invalidLng = new GpsLocationDto.CreateRequest(45.0, 190.0, 50.0, 180.0, 5.0, 100.0, LocalDateTime.now());
        Exception exLng = assertThrows(Exception.class, () -> method.invoke(service, invalidLng));
        assertTrue(exLng.getCause() instanceof GpsValidationException);
        assertEquals("Longitude must be between -180 and 180 degrees.", exLng.getCause().getMessage());

        // Negative Speed
        GpsLocationDto.CreateRequest invalidSpeed = new GpsLocationDto.CreateRequest(45.0, 90.0, -10.0, 180.0, 5.0, 100.0, LocalDateTime.now());
        Exception exSpeed = assertThrows(Exception.class, () -> method.invoke(service, invalidSpeed));
        assertTrue(exSpeed.getCause() instanceof GpsValidationException);
        assertEquals("Speed cannot be negative.", exSpeed.getCause().getMessage());
    }
}
