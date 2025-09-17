package com.example.zerowaste_api.converter;

import com.example.zerowaste_api.dto.FoodItemResDTO;
import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FoodItemConverter {

  public FoodItemResDTO toFoodItemResDTO(FoodItem foodItem) {
    if ( Objects.isNull(foodItem)) {
      return null;
    }
    FoodItemResDTO resDTO = new FoodItemResDTO();
    resDTO.setName(foodItem.getName());
    resDTO.setQuantity(foodItem.getQuantity());
    resDTO.setExpiryDate(foodItem.getExpiryDate());
    resDTO.setCategory(foodItem.getCategory());
    resDTO.setStorageLocation(foodItem.getStorageLocation());
    resDTO.setRemarks(foodItem.getRemarks());
    resDTO.setContactMethod(foodItem.getContactMethod());
    resDTO.setPickupLocation(foodItem.getPickupLocation());
    resDTO.setActionType(foodItem.getActionType());
    resDTO.setUser(foodItem.getUser());
    resDTO.setConvertToDonation(foodItem.getConvertToDonation());
    resDTO.setReservedQuantity(foodItem.getReservedQuantity());
    return resDTO;
  }
}
