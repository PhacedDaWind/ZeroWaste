package com.example.zerowaste_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verify2faRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String code;
}