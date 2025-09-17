package com.example.zerowaste_api.service;

import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.FoodItemDAO;
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
        FoodItem foodItem = foodItemDAO.findById(id).orElse(null);
        return foodItemConverter.toFoodItemResDTO(foodItem);
    }
}
