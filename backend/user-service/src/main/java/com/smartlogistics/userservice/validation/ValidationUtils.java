package com.smartlogistics.userservice.validation;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern GST_PATTERN = Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
    private static final Pattern PIN_CODE_PATTERN = Pattern.compile("^[1-9][0-9]{5}$");
    private static final Pattern INDIAN_MOBILE_PATTERN = Pattern.compile("^(\\+91)?[6-9]\\d{9}$");
    private static final Pattern AADHAAR_PATTERN = Pattern.compile("^\\d{12}$");

    public static boolean isValidGst(String gst) {
        return gst != null && GST_PATTERN.matcher(gst.trim()).matches();
    }

    public static boolean isValidPan(String pan) {
        return pan != null && PAN_PATTERN.matcher(pan.trim().toUpperCase()).matches();
    }

    public static boolean isValidPinCode(String pinCode) {
        return pinCode != null && PIN_CODE_PATTERN.matcher(pinCode.trim()).matches();
    }

    public static boolean isValidIndianMobile(String mobile) {
        return mobile != null && INDIAN_MOBILE_PATTERN.matcher(mobile.trim()).matches();
    }

    public static boolean isValidAadhaar(String aadhaar) {
        return aadhaar != null && AADHAAR_PATTERN.matcher(aadhaar.trim()).matches();
    }

    public static boolean isValidDocumentNumber(String type, String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return false;
        }
        String cleanNum = documentNumber.trim();
        return switch (type.toUpperCase()) {
            case "AADHAAR" -> isValidAadhaar(cleanNum);
            case "PAN" -> isValidPan(cleanNum);
            case "GST", "GST_CERTIFICATE" -> isValidGst(cleanNum);
            case "LICENSE", "DRIVING_LICENSE", "DRIVERS_LICENSE" -> cleanNum.length() >= 5 && cleanNum.length() <= 30; // DL formats vary across states
            default -> cleanNum.length() >= 5 && cleanNum.length() <= 50; // generic validation
        };
    }
}
