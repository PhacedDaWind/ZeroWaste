package com.example.zerowaste_api.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BrowseFoodItemResDTO {
    private Long id;

    private Long userId;

    private Boolean convertToDonation;

    private String category;

    private LocalDate expiryDate;

    private String storageLocation;

    private String name;

    private Long quantity;

    private String pickupLocation;

    private String contactMethod;
}
