package com.example.zerowaste_api.dto;

import java.time.LocalDate;

public interface BrowseFoodItemTuple {
    Long getId();

    Long getUsersId();

    Boolean getConvertToDonation();

    String getCategory();

    LocalDate getExpiryDate();

    String getStorageLocation();

    String getUsername();
    String getItemName();

    Long getQuantity();

    String getPickupLocation();

    String getContactMethod();
}
