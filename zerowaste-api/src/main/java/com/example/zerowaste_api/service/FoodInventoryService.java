package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.FoodItemErrorConstant;
import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.FoodItemDAO;
import com.example.zerowaste_api.dto.FoodItemReqDTO;
import com.example.zerowaste_api.dto.FoodItemResDTO;
import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.NotificationType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FoodInventoryService {
    private final FoodItemDAO foodItemDAO;
    private final FoodItemConverter foodItemConverter;
    private final NotificationService notificationService;

    public FoodInventoryService(FoodItemDAO foodItemDAO, FoodItemConverter foodItemConverter, NotificationService notificationService) {
        this.foodItemDAO = foodItemDAO;
        this.foodItemConverter = foodItemConverter;
        this.notificationService = notificationService;
    }

    public FoodItemResDTO read(Long id) {
        FoodItem foodItem = foodItemDAO.findById(id);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }

    public FoodItemResDTO create(FoodItemReqDTO foodItemReqDTO) {
        FoodItem foodItem = new FoodItem();
        foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
        if (foodItemReqDTO.getConvertToDonation().equals(Boolean.TRUE)) {
            notificationService.create(NotificationType.DONATION_POSTED, foodItem.getUser().getId(), foodItem.getName(), foodItem.getQuantity(), foodItem.getExpiryDate(), null);
        }
        foodItemDAO.save(foodItem);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }

    public FoodItemResDTO update(Long id, FoodItemReqDTO foodItemReqDTO) {
        FoodItem foodItem = foodItemDAO.findById(id);
        if (foodItemReqDTO.getConvertToDonation().equals(Boolean.TRUE) && !foodItem.getConvertToDonation().equals(foodItemReqDTO.getConvertToDonation())) {
            notificationService.create(NotificationType.DONATION_POSTED, foodItem.getUser().getId(), foodItem.getName(), foodItem.getQuantity(), foodItem.getExpiryDate(), null);
        }
        foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
        foodItemDAO.save(foodItem);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }

    public void delete(Long id) {
        if ( Objects.isNull(id)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, FoodItemErrorConstant.FOOD_ITEM_NOT_FOUND);
        }
        foodItemDAO.delete(id);
    }
}
