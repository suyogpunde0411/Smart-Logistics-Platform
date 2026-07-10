package com.smartlogistics.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class UserDTO {

    public record UserResponse(
            UUID id,
            String email,
            String phone,
            String firstName,
            String lastName,
            String status
    ) {}

    public record UserUpdateRequest(
            @NotBlank(message = "First name is mandatory")
            @Size(max = 50)
            String firstName,

            @NotBlank(message = "Last name is mandatory")
            @Size(max = 50)
            String lastName,

            @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone format")
            String phone
    ) {}
}
