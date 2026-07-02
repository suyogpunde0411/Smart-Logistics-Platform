package com.smartlogistics.auth.service;

import com.smartlogistics.auth.domain.entity.*;
import com.smartlogistics.auth.domain.repository.*;
import com.smartlogistics.auth.dto.AuthDto.*;
import com.smartlogistics.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserCredentialRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final EmailService emailService;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenDurationMs;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }
        
        Role role = roleRepository.findByName(request.role().toUpperCase())
                .orElseGet(() -> {
                    Role r = Role.builder().name(request.role().toUpperCase()).build();
                    return roleRepository.save(r);
                });

        UserCredential user = UserCredential.builder()
                .email(request.email())
                .phone(request.phone())
                .passwordHash(passwordEncoder.encode(request.password()))
                .enabled(true)
                .roles(Set.of(role))
                .build();

        user = userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getEmail());

        return createAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request, String ipAddress) {
        int attempts = redisService.getLoginAttempts(ipAddress);
        if (attempts >= 5) {
            throw new RuntimeException("Account locked due to too many failed attempts");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            redisService.resetLoginAttempts(ipAddress);
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            return createAuthResponse(userDetails.getUserCredential());
        } catch (Exception e) {
            redisService.incrementLoginAttempts(ipAddress);
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Transactional
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now()) || refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return createAuthResponse(refreshToken.getUser());
    }

    public void logout(String authHeader, String refreshTokenString) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            redisService.blacklistToken(jwt, 900000); // 15 mins
        }
        
        if (refreshTokenString != null) {
            refreshTokenRepository.findByToken(refreshTokenString).ifPresent(token -> {
                token.setRevoked(true);
                refreshTokenRepository.save(token);
            });
        }
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            // In reality, save token to DB PasswordResetTokenRepository
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });
    }

    private AuthResponse createAuthResponse(UserCredential user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String jwt = jwtService.generateToken(userDetails);
        
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusNanos(refreshTokenDurationMs * 1000000))
                .build();
        
        refreshTokenRepository.save(refreshToken);
        
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(jwt, refreshToken.getToken(), user.getEmail(), roles);
    }
}
