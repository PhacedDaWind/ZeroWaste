package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public interface UserDetailsTuple {

    Long getId();
    String getUsername();

    String getEmail();

    Long getHouseholdSize();

    Boolean getTwoFactorAuthEnabled();

    Status getStatus();

    Long getDonationsMade();

    Long getTotalItems();
}
