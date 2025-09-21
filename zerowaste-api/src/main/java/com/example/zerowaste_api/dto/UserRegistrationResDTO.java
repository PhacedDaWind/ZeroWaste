package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationResDTO {
    private String username;
    private String email;
    private Long householdSize;
    private Boolean twoFactorAuthEnabled;
    private Status status;
}
