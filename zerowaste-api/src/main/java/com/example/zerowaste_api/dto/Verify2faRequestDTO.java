package com.example.zerowaste_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verify2faRequestDTO {

    @NotBlank(message = "Verification code is required.")
    @Size(min = 6, max = 6, message = "Code must be 6 digits.")
    private String verificationCode;
}