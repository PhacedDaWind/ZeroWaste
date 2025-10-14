package com.example.zerowaste_api.converter;

import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.*;
import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BrowseFoodItemConverter {

    public List<BrowseFoodItemResDTO> toBrowseFoodItemResDTO(List<BrowseFoodItemTuple> browseFoodItemTuple) {
        return browseFoodItemTuple.stream()
                .map(tuples->{
                    BrowseFoodItemResDTO resDTO = new BrowseFoodItemResDTO();
                    resDTO.setId(tuples.getId());
                    resDTO.setUsersId(tuples.getUsersId());
                    resDTO.setConvertToDonation(tuples.getConvertToDonation());
                    resDTO.setCategory(tuples.getCategory());
                    resDTO.setExpiryDate(tuples.getExpiryDate());
                    resDTO.setStorageLocation(tuples.getStorageLocation());
                    resDTO.setUsername(tuples.getUsername());
                    resDTO.setItemName(tuples.getItemName());
                    resDTO.setQuantity(tuples.getQuantity());
                    resDTO.setPickupLocation(tuples.getPickupLocation());
                    resDTO.setContactMethod(tuples.getContactMethod());
                    resDTO.setActionType(tuples.getActionType());
                    resDTO.setActionTypeLabel(Objects.nonNull(tuples.getActionType()) ? tuples.getActionType().getLabel() : null);
                    return resDTO;
                        })
                .collect(Collectors.toList());
    }

    public BrowseFoodItemATResDTO browseFoodItemATResDTO(FoodItem foodItem) {
        if(Objects.isNull(foodItem)) {
            return null;
        }
        BrowseFoodItemATResDTO resDTO = new BrowseFoodItemATResDTO();
        resDTO.setName(foodItem.getName());
        resDTO.setQuantity(foodItem.getQuantity());
        resDTO.setPickupLocation(foodItem.getPickupLocation());
        resDTO.setContactMethod(foodItem.getContactMethod());
        resDTO.setActionType(foodItem.getActionType());
        return resDTO;
    }
}
