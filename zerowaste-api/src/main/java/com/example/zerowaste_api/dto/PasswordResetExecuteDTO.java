package com.example.zerowaste_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetExecuteDTO {
    private String email;

    private String code;

    private String newPassword;
}