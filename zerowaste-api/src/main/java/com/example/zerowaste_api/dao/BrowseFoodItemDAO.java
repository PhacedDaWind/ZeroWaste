package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.dto.BrowseFoodItemTuple;
import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.repository.BrowseFoodItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class BrowseFoodItemDAO {
    private final BrowseFoodItemRepository browseFoodItemRepository;

    public BrowseFoodItemDAO(BrowseFoodItemRepository browseFoodItemRepository) {
        this.browseFoodItemRepository = browseFoodItemRepository;
    }

    public Page<BrowseFoodItemTuple> getPage(Pageable pageable,
                                             Long usersId,
                                             Boolean convertToDonation,
                                             String category,
                                             LocalDate expiryDate,
                                             String storageLocation) {
        return browseFoodItemRepository.getBrowse(pageable,
                usersId,
                convertToDonation,
                category,
                expiryDate,
                storageLocation);
    }
}
