package com.example.zerowaste_api.entity;

import com.example.zerowaste_api.common.BaseDomain;
import com.example.zerowaste_api.enums.FoodItemActionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food_item")
public class FoodItem extends BaseDomain {

  private String name;

  private Long quantity;

  private LocalDate expiryDate;

  private String category;

  private String storageLocation;

  private String remarks;

  private String contactMethod;

  private String pickupLocation;

  @Column(name = "action_type")
  @Enumerated(EnumType.STRING)
  private FoodItemActionType actionType;

  @ManyToOne
  @JoinColumn(name = "users_id")
  private Users user;

  private Boolean convertToDonation;

  private Long reservedQuantity;
}
