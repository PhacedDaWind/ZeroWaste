package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.dto.PasswordResetExecuteDTO;
import com.example.zerowaste_api.dto.PasswordResetRequestDTO;
import com.example.zerowaste_api.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController extends BaseController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    public ResponseEntity<ResponseDTO<String>> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO requestDTO) {
        passwordResetService.initiatePasswordReset(requestDTO.getEmail());
        // Always return a generic success message to prevent attackers from discovering registered emails
        ResponseDTO<String> response = createResponse(HttpStatus.OK, "If an account with that email exists, a password reset code has been sent.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/execute")
    public ResponseEntity<ResponseDTO<String>> executePasswordReset(@Valid @RequestBody PasswordResetExecuteDTO executeDTO) {
        passwordResetService.finalizePasswordReset(executeDTO.getEmail(), executeDTO.getCode(), executeDTO.getNewPassword());
        ResponseDTO<String> response = createResponse(HttpStatus.OK, "Your password has been reset successfully.");
        return ResponseEntity.ok(response);
    }
}