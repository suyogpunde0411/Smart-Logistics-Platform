package com.smartlogistics.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class AddressDto {

    public record AddressResponse(
            UUID id,
            String line1,
            String line2,
            String city,
            String state,
            String zip,
            String country,
            String type
    ) {}

    public record AddressRequest(
            @NotBlank(message = "Address line 1 is mandatory")
            @Size(max = 255)
            String line1,

            @Size(max = 255)
            String line2,

            @NotBlank(message = "City is mandatory")
            @Size(max = 100)
            String city,

            @NotBlank(message = "State is mandatory")
            @Size(max = 100)
            String state,

            @NotBlank(message = "Zip/PIN code is mandatory")
            @Size(max = 20)
            String zip,

            @NotBlank(message = "Country is mandatory")
            @Size(max = 100)
            String country,

            @NotBlank(message = "Address type is mandatory")
            @Size(max = 50)
            String type // HOME, BUSINESS, BILLING, SHIPPING
    ) {}
}
