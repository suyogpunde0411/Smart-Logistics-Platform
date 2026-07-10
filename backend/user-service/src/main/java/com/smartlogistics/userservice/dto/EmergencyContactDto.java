package com.smartlogistics.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class EmergencyContactDto {

    public record EmergencyContactResponse(
            UUID id,
            String name,
            String phone,
            String relationship
    ) {}

    public record EmergencyContactRequest(
            @NotBlank(message = "Name is mandatory")
            @Size(max = 100)
            String name,

            @NotBlank(message = "Phone number is mandatory")
            @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone format")
            String phone,

            @Size(max = 50)
            String relationship
    ) {}
}
