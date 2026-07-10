package com.smartlogistics.shared.util;

import com.smartlogistics.shared.exception.ValidationException;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern GST_PATTERN = Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern PIN_PATTERN = Pattern.compile("^[1-9][0-9]{5}$");
    private static final Pattern VEHICLE_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$");

    public static void validateCapacity(Double capacity, String fieldName) {
        if (capacity == null || capacity <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero");
        }
    }

    public static void validateWeight(Double weight, String fieldName) {
        if (weight == null || weight <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero");
        }
    }

    public static void validateDimension(Double dim, String fieldName) {
        if (dim == null || dim <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero");
        }
    }

    public static void validateGst(String gst) {
        if (gst == null || !GST_PATTERN.matcher(gst).matches()) {
            throw new ValidationException("Invalid GST number format");
        }
    }

    public static void validatePan(String pan) {
        if (pan == null || !PAN_PATTERN.matcher(pan).matches()) {
            throw new ValidationException("Invalid PAN number format");
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone number format");
        }
    }

    public static void validatePin(String pin) {
        if (pin == null || !PIN_PATTERN.matcher(pin).matches()) {
            throw new ValidationException("Invalid PIN code format");
        }
    }

    public static void validateVehicleNumber(String number) {
        if (number == null || !VEHICLE_PATTERN.matcher(number).matches()) {
            throw new ValidationException("Invalid vehicle number format");
        }
    }

    public static void validateCoordinates(Double lat, Double lng) {
        if (lat == null || lat < -90.0 || lat > 90.0) {
            throw new ValidationException("Latitude must be between -90 and 90 degrees");
        }
        if (lng == null || lng < -180.0 || lng > 180.0) {
            throw new ValidationException("Longitude must be between -180 and 180 degrees");
        }
    }
}
