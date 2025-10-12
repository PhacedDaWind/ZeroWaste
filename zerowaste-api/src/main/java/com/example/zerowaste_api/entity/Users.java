package com.example.zerowaste_api.entity;

import com.example.zerowaste_api.common.BaseDomain;
import com.example.zerowaste_api.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users extends BaseDomain {

    private String username;

    private String password;

    private String email;

    private Long householdSize;

    private String passwordResetCode;

    private Boolean twoFactorAuthEnabled;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime twoFactorExpiresAt;
}
