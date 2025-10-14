package com.example.zerowaste_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void send2faCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@zerowasteapp.com"); // Set your "from" address
            message.setTo(to);
            message.setSubject("Your Two-Factor Authentication Code");
            message.setText("Welcome to the Zero Waste App!\n\nYour verification code is: " + code + "\n\nThis code will expire in 5 minutes.");

            // For debugging purposes, log the email
            log.info("Attempting to send 2FA email to: {}", to);
            log.info("Email Body: {}", message.getText());

            mailSender.send(message);

            log.info("2FA email sent successfully to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send 2FA email to: {}", to, e);
            // In a real application, you might want to re-throw this as a custom exception
            throw new RuntimeException("Error while sending email. Please try again later.");
        }
    }

    public void sendPasswordResetCode(String email, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@zerowasteapp.com"); // Set your "from" address
            message.setTo(email);
            message.setSubject("Your Password Reset Code");
            message.setText("Enter the verification code below in the app.\n\nYour verification code is: " + code + "\n\nThis code will expire in 5 minutes.");

            // For debugging purposes, log the email
            log.info("Attempting to send password reset email to: {}", email);
            log.info("Email Body: {}", message.getText());

            mailSender.send(message);

            log.info("Password reset email sent successfully to: {}", email);
        } catch (MailException e) {
            log.error("Failed to send password reset email to: {}", email, e);
            // In a real application, you might want to re-throw this as a custom exception
            throw new RuntimeException("Error while sending email. Please try again later.");
        }
    }
}