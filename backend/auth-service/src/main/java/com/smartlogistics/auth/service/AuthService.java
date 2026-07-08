package com.smartlogistics.auth.service;

import com.smartlogistics.auth.domain.entity.*;
import com.smartlogistics.auth.domain.repository.*;
import com.smartlogistics.auth.dto.AuthDto.*;
import com.smartlogistics.auth.exception.*;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserSessionRepository userSessionRepository;
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
                .enabled(false) // Wait, backward compat? I'll set to false, per standard.
                .roles(Set.of(role))
                .build();

        user = userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getEmail());
        
        // Auto send email verification
        sendEmailVerification(user.getEmail());

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request, String ipAddress, String userAgent) {
        int attempts = redisService.getLoginAttempts(ipAddress);
        if (attempts >= 5) {
            throw new AccountLockedException("Account locked due to too many failed attempts");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            redisService.resetLoginAttempts(ipAddress);
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            UserCredential user = userDetails.getUserCredential();
            
            if (!user.isEnabled()) {
                throw new EmailNotVerifiedException("Email is not verified");
            }
            
            // Record UserSession
            UserSession session = UserSession.builder()
                    .user(user)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .device(extractDevice(userAgent))
                    .location("Unknown Location") // Location Placeholder
                    .build();
            userSessionRepository.save(session);
            
            return createAuthResponse(user);
        } catch (EmailNotVerifiedException e) {
            throw e;
        } catch (Exception e) {
            redisService.incrementLoginAttempts(ipAddress);
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    private String extractDevice(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet")) return "Tablet";
        return "Desktop";
    }

    @Transactional
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new TokenExpiredException("Refresh token not found"));
        
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now()) || refreshToken.isRevoked()) {
            throw new TokenExpiredException("Refresh token expired or revoked");
        }

        // Token rotation: revoke old, create new
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        
        return createAuthResponse(refreshToken.getUser());
    }

    @Transactional
    public void logout(String authHeader, String refreshTokenString) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            redisService.blacklistToken(jwt, 900000); // 15 mins
            
            // Extract user email from JWT to close session
            String email = jwtService.extractUsername(jwt);
            userRepository.findByEmail(email).ifPresent(user -> {
                userSessionRepository.findTopByUserOrderByLoginTimeDesc(user).ifPresent(session -> {
                    session.setLogoutTime(LocalDateTime.now());
                    session.setActive(false);
                    userSessionRepository.save(session);
                });
            });
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
            PasswordResetToken prt = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
            passwordResetTokenRepository.save(prt);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid token"));
        
        if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired or already used");
        }
        
        UserCredential user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
    
    @Transactional
    public void changePassword(ChangePasswordRequest request, String email) {
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void sendEmailVerification(String email) {
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        if (user.isEnabled()) {
            throw new RuntimeException("Email already verified");
        }
        
        String token = UUID.randomUUID().toString();
        EmailVerificationToken evt = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        emailVerificationTokenRepository.save(evt);
        
        emailService.sendVerificationEmail(user.getEmail(), token);
    }
    
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        EmailVerificationToken token = emailVerificationTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid token"));
                
        if (token.isUsed() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired or already used");
        }
        
        UserCredential user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        
        token.setUsed(true);
        emailVerificationTokenRepository.save(token);
    }
    
    public void sendOtp(OtpRequest request) {
        // Mock OTP generation
        String otp = "123456"; 
        redisService.saveOtp(request.identifier(), otp, 300000); // 5 mins
        emailService.sendOtpEmail(request.identifier(), otp);
    }
    
    public void verifyOtp(VerifyOtpRequest request) {
        String savedOtp = redisService.getOtp(request.identifier());
        if (savedOtp == null) {
            throw new OtpExpiredException("OTP expired or not found");
        }
        if (!savedOtp.equals(request.otp())) {
            throw new OtpInvalidException("Invalid OTP");
        }
        redisService.deleteOtp(request.identifier());
    }
    
    public MeResponse getMe(String email) {
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
                
        return new MeResponse(user.getEmail(), user.getPhone(), roles);
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
