package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationReqDTO {
    private String username;
    private String password;
    private String email;
    private Long householdSize;
    private Boolean twoFactorAuthEnabled;
    private Status status;
}
