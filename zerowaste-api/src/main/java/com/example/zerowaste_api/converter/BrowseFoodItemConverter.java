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
                .filter(Objects::nonNull)
                .map(tuples->{
                    BrowseFoodItemResDTO resDTO = new BrowseFoodItemResDTO();
                    resDTO.setName(tuples.getUserName());
                    resDTO.setQuantity(tuples.getQuantity());
                    resDTO.setExpiryDate(tuples.getExpiryDate());
                    resDTO.setContactMethod(tuples.getContactMethod());
                    resDTO.setPickupLocation(tuples.getPickupLocation());
                    return resDTO;
                        })
                .collect(Collectors.toList());
    }
}
