package com.example.zerowaste_api.dto;

import java.time.LocalDate;

public interface BrowseFoodItemTuple {
    Long getId();

    String getUserName();

    Long getQuantity();

    LocalDate getExpiryDate();

    String getPickupLocation();

    String getContactMethod();
}
