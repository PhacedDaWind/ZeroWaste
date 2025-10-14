package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.entity.Users;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityService {

  private static final int CODE_EXPIRATION_MINUTES = 5;

  private final UsersDAO usersDAO;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService; // Assuming you have this service

  public SecurityService(
      UsersDAO usersDAO, PasswordEncoder passwordEncoder, EmailService emailService) {
    this.usersDAO = usersDAO;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  @Transactional
  public void initiate2faSetup(String username) {
    Users user =
        usersDAO
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

    String code = String.format("%06d", new SecureRandom().nextInt(999999));
    LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);

    // Using your entity's field names
    user.setPasswordResetCode(passwordEncoder.encode(code));
    user.setTwoFactorExpiresAt(expiryTime);
    usersDAO.save(user);

    emailService.send2faCode(user.getEmail(), code);
  }

  @Transactional
  public void verify2faSetup(String username, String verificationCode, String newPassword) {
    Users user =
        usersDAO
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

    if (user.getPasswordResetCode() == null
        || user.getTwoFactorExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ServiceAppException(
          HttpStatus.BAD_REQUEST, "Verification code is invalid or has expired.");
    }

    if (!passwordEncoder.matches(verificationCode, user.getPasswordResetCode())) {
      throw new IllegalArgumentException("Incorrect verification code.");
    }

    // On success, update user record
    user.setTwoFactorAuthEnabled(true);
    user.setPassword(passwordEncoder.encode(newPassword));

    // Clear temporary code fields
    user.setPasswordResetCode(null);
    user.setTwoFactorExpiresAt(null);

    usersDAO.save(user);
  }

  @Transactional
  public void generateAndSendLogin2faCode(String username) {
    Users user =
        usersDAO
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

    String code = String.format("%06d", new SecureRandom().nextInt(999999));
    LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);

    user.setPasswordResetCode(passwordEncoder.encode(code));
    user.setTwoFactorExpiresAt(expiryTime);
    usersDAO.save(user);

    emailService.send2faCode(user.getEmail(), code);
  }

  /** Verifies the 2FA code during login and returns the UserDetails if valid. */
  public Users verifyLogin2faCode(String username, String code) {
    Users user =
        usersDAO
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

    if (user.getPasswordResetCode() == null
        || user.getTwoFactorExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ServiceAppException(
          HttpStatus.BAD_REQUEST, "Verification code is invalid or has expired.");
    }

    if (!passwordEncoder.matches(code, user.getPasswordResetCode())) {
      throw new IllegalArgumentException("Incorrect verification code.");
    }

    // Clear the code after successful use
    user.setPasswordResetCode(null);
    user.setTwoFactorExpiresAt(null);
    usersDAO.save(user);

    return user;
  }
}
