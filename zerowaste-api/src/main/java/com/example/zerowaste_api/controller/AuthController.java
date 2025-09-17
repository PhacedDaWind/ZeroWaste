package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.dto.LoginRequest;
import com.example.zerowaste_api.dto.LoginResponse;
import com.example.zerowaste_api.entity.Users;
import com.example.zerowaste_api.repository.UserRepository;
import com.example.zerowaste_api.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // If authentication is successful, generate a token
        UserDetails userDetails = loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch your user entity from the database
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert your user entity into Spring Security's UserDetails object
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>()); // Using an empty list for authorities for now
    }
}