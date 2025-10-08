package com.example.zerowaste_api.converter;

import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.FoodItemReqDTO;
import com.example.zerowaste_api.dto.FoodItemResDTO;
import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FoodItemConverter {

  private final UsersConverter usersConverter;

  private final UsersDAO usersDAO;

    public FoodItemConverter(UsersConverter usersConverter, UsersDAO usersDAO) {
        this.usersConverter = usersConverter;
      this.usersDAO = usersDAO;
    }

    public FoodItemResDTO toFoodItemResDTO(FoodItem foodItem) {
    if ( Objects.isNull(foodItem)) {
      return null;
    }
    FoodItemResDTO resDTO = new FoodItemResDTO();
    resDTO.setId(foodItem.getId());
    resDTO.setName(foodItem.getName());
    resDTO.setQuantity(foodItem.getQuantity());
    resDTO.setExpiryDate(foodItem.getExpiryDate());
    resDTO.setCategory(foodItem.getCategory());
    resDTO.setStorageLocation(foodItem.getStorageLocation());
    resDTO.setRemarks(foodItem.getRemarks());
    resDTO.setContactMethod(foodItem.getContactMethod());
    resDTO.setPickupLocation(foodItem.getPickupLocation());
    resDTO.setActionType(foodItem.getActionType());
    resDTO.setUser(usersConverter.toUserResponseDTO(foodItem.getUser()));
    resDTO.setConvertToDonation(foodItem.getConvertToDonation());
    resDTO.setReservedQuantity(foodItem.getReservedQuantity());
    return resDTO;
  }

  public FoodItem toFoodItem(FoodItemReqDTO foodItemReqDTO, FoodItem foodItem) {
      if (Objects.isNull(foodItemReqDTO)) {
        return null;
      }
      foodItem.setName(foodItemReqDTO.getName());
    foodItem.setQuantity(foodItem.getQuantity());
    foodItem.setExpiryDate(foodItem.getExpiryDate());
    foodItem.setCategory(foodItem.getCategory());
    foodItem.setStorageLocation(foodItem.getStorageLocation());
    foodItem.setRemarks(foodItem.getRemarks());
    foodItem.setContactMethod(foodItem.getContactMethod());
    foodItem.setPickupLocation(foodItem.getPickupLocation());
    foodItem.setActionType(foodItem.getActionType());
    foodItem.setUser(usersDAO.findById(foodItemReqDTO.getUserId()));
    foodItem.setConvertToDonation(foodItem.getConvertToDonation());
    foodItem.setReservedQuantity(foodItem.getReservedQuantity());
    return foodItem;
  }
}
