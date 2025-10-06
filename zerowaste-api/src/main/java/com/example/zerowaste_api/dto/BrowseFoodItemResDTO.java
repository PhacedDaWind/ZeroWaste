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
    private String name;

    private BigDecimal quantity;

    private LocalDate expiryDate;

    private String pickupLocation;

    private String contactMethod;
}
