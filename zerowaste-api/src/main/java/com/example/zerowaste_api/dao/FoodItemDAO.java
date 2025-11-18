package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.FoodItemErrorConstant;
import com.example.zerowaste_api.common.error.UserErrorConstant;
import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.repository.FoodItemRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FoodItemDAO {
  private final FoodItemRepository foodItemRepository;

  public FoodItemDAO(FoodItemRepository foodItemRepository) {
    this.foodItemRepository = foodItemRepository;
  }

  public FoodItem save(FoodItem foodItem) {
    if (Objects.isNull(foodItem)) {
      return null;
    }
    return foodItemRepository.save(foodItem);
  }

  public FoodItem findById(Long id) {
    return foodItemRepository.findById(id).orElseThrow(
            () -> new ServiceAppException(HttpStatus.BAD_REQUEST, FoodItemErrorConstant.FOOD_ITEM_NOT_FOUND));
  }

  public void delete(Long id) {
    if (Objects.isNull(id)) {
      throw new ServiceAppException(HttpStatus.BAD_REQUEST, FoodItemErrorConstant.FOOD_ITEM_NOT_FOUND);
    }
    foodItemRepository.deleteById(id);
  }

  public Optional<FoodItem> findDuplicateItem(Long userId, String name, LocalDate expiryDate, Boolean isDonation) {
    return foodItemRepository.findDuplicateItem(userId, name, expiryDate, isDonation);
  }

  public List<FoodItem> findAllByUserIdAndConvertToDonationFalseAndExpiryDateBetweenOrderByExpiryDateAsc(Long userId, LocalDate today, LocalDate limit) {
    return foodItemRepository.findAllByUserIdAndConvertToDonationFalseAndExpiryDateBetweenOrderByExpiryDateAsc(userId, today, limit);
  }
}
