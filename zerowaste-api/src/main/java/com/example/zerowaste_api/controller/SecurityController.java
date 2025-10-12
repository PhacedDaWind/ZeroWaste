package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.dto.Verify2faRequestDTO;
import com.example.zerowaste_api.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/security")
public class SecurityController extends BaseController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/enable-2fa")
    public ResponseDTO<?> enable2fa(@AuthenticationPrincipal UserDetails userDetails) {
        // Get username from the authenticated principal
        String username = userDetails.getUsername();
        securityService.initiate2faSetup(username);
        return createResponse(HttpStatus.OK, "A verification code has been sent to your email.");
    }

    @PostMapping("/verify-2fa-setup")
    public ResponseDTO<?> verify2faSetup(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Verify2faRequestDTO request) {

        try {
            String username = userDetails.getUsername();
            securityService.verify2faSetup(username, request.getVerificationCode(), request.getNewPassword());
            return createResponse(HttpStatus.OK, "Two-factor authentication has been enabled successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
      // Return a structured error response for invalid/expired codes
      throw new ServiceAppException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}