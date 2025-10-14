package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsResDTO {

    private Long id;
    private String username;

    private String email;

    private Long householdSize;

    private Boolean twoFactorAuthEnabled;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private Long totalItems;

    private Long donationsMade;
}
