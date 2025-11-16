package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.FoodItemActionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrowseFoodItemATResDTO {
    private String name;

    private Long quantity;

    private String pickupLocation;

    private String contactMethod;

    private Boolean convertToDonation;

    private FoodItemActionType actionType;

    private Long userId;
}
