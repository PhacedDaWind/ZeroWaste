package com.example.zerowaste_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // This ensures null fields are not in the JSON response
public class LoginResponse {

    private String status;
    private String message;
    private String token;

    // Constructor for a successful login
    public LoginResponse(String token) {
        this.status = "SUCCESS";
        this.message = "Login successful.";
        this.token = token;
    }

    // Constructor for when 2FA is required
    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}