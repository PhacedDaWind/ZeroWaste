package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.entity.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class PasswordResetService {
    private static final int CODE_EXPIRATION_MINUTES = 5;

    private final UsersDAO usersDAO;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(UsersDAO usersDAO, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usersDAO = usersDAO;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        // Find user by email. If not found, we don't throw an error to prevent email enumeration attacks.
        usersDAO.findUserByEmail(email).ifPresent(user -> {
            String code = String.format("%06d", new SecureRandom().nextInt(999999));
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);

            user.setPasswordResetCode(passwordEncoder.encode(code));
            user.setTwoFactorExpiresAt(expiryTime);
            usersDAO.save(user);

            // You might want to create a new email template/method for this in EmailService
            emailService.sendPasswordResetCode(user.getEmail(), code);
        });
    }

    @Transactional
    public void finalizePasswordReset(String email, String code, String newPassword) {
        Users user = usersDAO.findUserByEmail(email)
                .orElseThrow(() -> new ServiceAppException(HttpStatus.BAD_REQUEST, "Email not found"));

        if (user.getPasswordResetCode() == null || user.getTwoFactorExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Password reset code is invalid or has expired.");
        }

        if (!passwordEncoder.matches(code, user.getPasswordResetCode())) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Incorrect password reset code.");
        }

        // Success: Update password and clear the reset code
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetCode(null);
        user.setTwoFactorExpiresAt(null);
        usersDAO.save(user);
    }
}