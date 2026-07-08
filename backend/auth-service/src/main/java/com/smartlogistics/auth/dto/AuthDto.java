package com.smartlogistics.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthDto() {

    public record RegisterRequest(
            @NotBlank @Email String email,
            @NotBlank String phone,
            @NotBlank @Size(min = 8) String password,
            @NotBlank String role
    ) {}

    public record LoginRequest(
            @NotBlank String email,
            @NotBlank String password
    ) {}

    public record TokenRefreshRequest(
            @NotBlank String refreshToken
    ) {}

    public record ForgotPasswordRequest(
            @NotBlank @Email String email
    ) {}

    public record ResetPasswordRequest(
            @NotBlank String token,
            @NotBlank @Size(min = 8) String newPassword
    ) {}

    public record ChangePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank @Size(min = 8) String newPassword
    ) {}

    public record OtpRequest(
            @NotBlank String identifier
    ) {}

    public record VerifyOtpRequest(
            @NotBlank String identifier,
            @NotBlank String otp
    ) {}
    public record VerifyEmailRequest(
            @NotBlank String token
    ) {}
    
    public record MeResponse(
            String email,
            String phone,
            java.util.Set<String> roles
    ) {}

    public record AuthResponse(
            String accessToken,
            String refreshToken,
            String email,
            java.util.Set<String> roles
    ) {}
}
