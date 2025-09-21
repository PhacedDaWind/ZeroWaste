package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.dto.LoginRequest;
import com.example.zerowaste_api.dto.LoginResponse;
import com.example.zerowaste_api.service.JwtService;
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

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // This validates the credentials and uses the AuthenticationProvider we created
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Get the UserDetails from the successful authentication object (no extra DB call)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate the token
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}