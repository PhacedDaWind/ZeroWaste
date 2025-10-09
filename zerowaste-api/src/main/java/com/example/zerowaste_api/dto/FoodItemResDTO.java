package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.FoodItemActionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodItemResDTO {
  private Long id;
  private String name;

  private Long quantity;

  private LocalDate expiryDate;

  private String category;

  private String storageLocation;

  private String remarks;

  private String contactMethod;

  private String pickupLocation;

  private FoodItemActionType actionType;

  private UserResponseDTO user;

  private Boolean convertToDonation;

  private Long reservedQuantity;
}
