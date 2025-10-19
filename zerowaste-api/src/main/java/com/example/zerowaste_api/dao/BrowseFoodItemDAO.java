package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.BrowseErrorConstant;
import com.example.zerowaste_api.dto.BrowseFoodItemTuple;
import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.FoodItemActionType;
import com.example.zerowaste_api.repository.BrowseFoodItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class BrowseFoodItemDAO {
    private final BrowseFoodItemRepository browseFoodItemRepository;

    public BrowseFoodItemDAO(BrowseFoodItemRepository browseFoodItemRepository) {
        this.browseFoodItemRepository = browseFoodItemRepository;
    }

    public FoodItem save(FoodItem foodItem) {
        if(Objects.isNull(foodItem.getId())) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, BrowseErrorConstant.BROWSE_FOOD_ITEM_NOT_FOUND);
        }
        return browseFoodItemRepository.save(foodItem);
    }

    public Page<BrowseFoodItemTuple> getPage(Pageable pageable,
                                             Long usersId,
                                             Boolean convertToDonation,
                                             String itemName,
                                             String category,
                                             LocalDate expiryDate,
                                             String storageLocation,
                                             FoodItemActionType actionType) {
        return browseFoodItemRepository.getBrowse(pageable,
                usersId,
                convertToDonation,
                itemName,
                category,
                expiryDate,
                storageLocation,
                actionType);
    }
    public FoodItem getFoodItem(Long foodItemId) {
        return browseFoodItemRepository.findById(foodItemId).orElse(null);
    }
}
