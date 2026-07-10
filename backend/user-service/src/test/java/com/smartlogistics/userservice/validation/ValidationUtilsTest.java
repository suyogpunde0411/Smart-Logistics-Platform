package com.smartlogistics.userservice.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testIsValidGst_Success() {
        // Valid Indian GST structure
        assertTrue(ValidationUtils.isValidGst("22AAAAA0000A1Z5"));
        assertTrue(ValidationUtils.isValidGst("07AAAAA1111A1Z1"));
    }

    @Test
    void testIsValidGst_Failure() {
        // Invalid lengths or formats
        assertFalse(ValidationUtils.isValidGst(""));
        assertFalse(ValidationUtils.isValidGst(null));
        assertFalse(ValidationUtils.isValidGst("123456789012345"));
        assertFalse(ValidationUtils.isValidGst("22AAAAA0000A1A5")); // Z character missing
    }

    @Test
    void testIsValidPan_Success() {
        assertTrue(ValidationUtils.isValidPan("ABCDE1234F"));
        assertTrue(ValidationUtils.isValidPan("abcde1234f"));
    }

    @Test
    void testIsValidPan_Failure() {
        assertFalse(ValidationUtils.isValidPan(""));
        assertFalse(ValidationUtils.isValidPan(null));
        assertFalse(ValidationUtils.isValidPan("ABCDE1234"));
        assertFalse(ValidationUtils.isValidPan("ABCD12345F"));
    }

    @Test
    void testIsValidPinCode_Success() {
        assertTrue(ValidationUtils.isValidPinCode("400001"));
        assertTrue(ValidationUtils.isValidPinCode("110001"));
    }

    @Test
    void testIsValidPinCode_Failure() {
        assertFalse(ValidationUtils.isValidPinCode("011001")); // PIN code starting with 0
        assertFalse(ValidationUtils.isValidPinCode("4000012")); // 7 digits
        assertFalse(ValidationUtils.isValidPinCode("4000")); // 4 digits
        assertFalse(ValidationUtils.isValidPinCode(null));
    }

    @Test
    void testIsValidIndianMobile_Success() {
        assertTrue(ValidationUtils.isValidIndianMobile("9876543210"));
        assertTrue(ValidationUtils.isValidIndianMobile("+919876543210"));
        assertTrue(ValidationUtils.isValidIndianMobile("7766554433"));
    }

    @Test
    void testIsValidIndianMobile_Failure() {
        assertFalse(ValidationUtils.isValidIndianMobile("5555555555")); // Doesn't start with 6-9
        assertFalse(ValidationUtils.isValidIndianMobile("987654321")); // Only 9 digits
        assertFalse(ValidationUtils.isValidIndianMobile("+9198765432101")); // Too long
        assertFalse(ValidationUtils.isValidIndianMobile(null));
    }

    @Test
    void testIsValidAadhaar_Success() {
        assertTrue(ValidationUtils.isValidAadhaar("123456789012"));
    }

    @Test
    void testIsValidAadhaar_Failure() {
        assertFalse(ValidationUtils.isValidAadhaar("12345678901")); // 11 digits
        assertFalse(ValidationUtils.isValidAadhaar("1234567890123")); // 13 digits
        assertFalse(ValidationUtils.isValidAadhaar("abc456789012"));
        assertFalse(ValidationUtils.isValidAadhaar(null));
    }

    @Test
    void testIsValidDocumentNumber() {
        assertTrue(ValidationUtils.isValidDocumentNumber("AADHAAR", "123456789012"));
        assertTrue(ValidationUtils.isValidDocumentNumber("PAN", "ABCDE1234F"));
        assertTrue(ValidationUtils.isValidDocumentNumber("GST", "22AAAAA0000A1Z5"));
        assertTrue(ValidationUtils.isValidDocumentNumber("LICENSE", "DL-1420110012345"));
        assertFalse(ValidationUtils.isValidDocumentNumber("AADHAAR", "123"));
    }
}
