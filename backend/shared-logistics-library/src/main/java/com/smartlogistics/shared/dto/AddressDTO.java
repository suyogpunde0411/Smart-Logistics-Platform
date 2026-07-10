

package com.smartlogistics.shared.dto;

public record AddressDTO(
        String line1,
        String line2,
        String city,
        String state,
        String country,
        String zip,
        String type
) {}
