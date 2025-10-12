package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.dto.LoginRequest;
import com.example.zerowaste_api.dto.LoginResponse;
import com.example.zerowaste_api.dto.Verify2faRequest;
import com.example.zerowaste_api.entity.Users;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.service.JwtService;
import com.example.zerowaste_api.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsersDAO usersDAO;
    private final SecurityService securityService;

    // Updated constructor
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsersDAO usersDAO, SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usersDAO = usersDAO;
        this.securityService = securityService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Step 1: Authenticate username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Step 2: Check if 2FA is enabled
        Users user = usersDAO.findByUsername(request.getUsername()).get();

        if (user.getTwoFactorAuthEnabled()) {
            // User has 2FA enabled, so send code and return an intermediate response
            securityService.generateAndSendLogin2faCode(user.getUsername());
            LoginResponse response = new LoginResponse("2FA_REQUIRED", "Please enter the code sent to your email.");
            return ResponseEntity.ok(response);
        } else {
            // User does not have 2FA, generate token and log them in directly
            String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
            LoginResponse response = new LoginResponse(token);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<LoginResponse> verify2fa(@Valid @RequestBody Verify2faRequest request) {
        try {
            // Verify the code
            Users user = securityService.verifyLogin2faCode(request.getUsername(), request.getCode());

            // If code is correct, generate the final JWT token
            // We need a UserDetails object to generate the token
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Password is not used for token generation but required by UserDetails
                    .authorities("USER") // Set appropriate roles/authorities
                    .build();

            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (IllegalStateException | IllegalArgumentException e) {
            LoginResponse errorResponse = new LoginResponse("ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}