package com.example.zerowaste_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetExecuteDTO {
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Code is required.")
    private String code;

    @NotBlank(message = "New password is required.")
    private String newPassword;
}