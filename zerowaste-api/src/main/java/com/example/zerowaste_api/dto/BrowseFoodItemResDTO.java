package com.example.zerowaste_api.dto;


import com.example.zerowaste_api.enums.FoodItemActionType;
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

    private Long usersId;

    private Boolean convertToDonation;

    private String category;

    private LocalDate expiryDate;

    private String storageLocation;

    private String username;
    private String itemName;

    private Long quantity;

    private String pickupLocation;

    private String contactMethod;

    private FoodItemActionType actionType;

    private String actionTypeLabel;
}
