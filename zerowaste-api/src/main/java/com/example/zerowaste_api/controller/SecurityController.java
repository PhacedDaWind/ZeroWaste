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

    @PostMapping("/enable-2fa/{id}")
    public ResponseDTO<?> enable2fa(@PathVariable Long id) {
        securityService.initiate2faSetup(id);
        return createResponse(HttpStatus.OK, "A verification code has been sent to your email.");
    }

    @PostMapping("/disable-2fa/{id}")
    public ResponseDTO<?> disable2fa(@PathVariable Long id) {
        // Get username from the authenticated principal
        securityService.disable2fa(id);
        return createResponse(HttpStatus.OK, "2FA has been disabled");
    }

    @PostMapping("/verify-2fa-setup/{id}")
    public ResponseDTO<?> verify2faSetup(
            @PathVariable Long id,
            @RequestBody Verify2faRequestDTO request) {

        try {

            securityService.verify2faSetup(id, request.getVerificationCode());
            return createResponse(HttpStatus.OK, "Two-factor authentication has been enabled successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
      // Return a structured error response for invalid/expired codes
      throw new ServiceAppException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}