package com.example.zerowaste_api.service;

import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.FoodItemDAO;
import com.example.zerowaste_api.dto.FoodItemReqDTO;
import com.example.zerowaste_api.dto.FoodItemResDTO;
import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.stereotype.Service;

@Service
public class FoodInventoryService {
    private final FoodItemDAO foodItemDAO;
    private final FoodItemConverter foodItemConverter;

    public FoodInventoryService(FoodItemDAO foodItemDAO, FoodItemConverter foodItemConverter) {
        this.foodItemDAO = foodItemDAO;
        this.foodItemConverter = foodItemConverter;
    }

    public FoodItemResDTO read(Long id) {
        FoodItem foodItem = foodItemDAO.findById(id);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }

    public FoodItemResDTO create(FoodItemReqDTO foodItemReqDTO) {
        FoodItem foodItem = new FoodItem();
        foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
        foodItemDAO.save(foodItem);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }

    public FoodItemResDTO update(Long id, FoodItemReqDTO foodItemReqDTO) {
        FoodItem foodItem = foodItemDAO.findById(id);
        foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
        foodItemDAO.save(foodItem);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }
}
