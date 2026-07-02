package com.smartlogistics.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {
        log.info("Sending welcome email to {}", toEmail);
        sendSimpleMessage(toEmail, "Welcome to Smart Logistics", "Hello " + name + ",\nWelcome to our platform!");
    }

    public void sendVerificationEmail(String toEmail, String token) {
        log.info("Sending verification email to {}", toEmail);
        String verificationUrl = "https://smartlogistics.com/verify?token=" + token;
        sendSimpleMessage(toEmail, "Verify Your Email", "Please click the link to verify your email: " + verificationUrl);
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        log.info("Sending password reset email to {}", toEmail);
        String resetUrl = "https://smartlogistics.com/reset-password?token=" + token;
        sendSimpleMessage(toEmail, "Reset Your Password", "Please click the link to reset your password: " + resetUrl);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("Sending OTP email to {}", toEmail);
        sendSimpleMessage(toEmail, "Your OTP Code", "Your OTP code is: " + otp + ". It is valid for 5 minutes.");
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@smartlogistics.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
