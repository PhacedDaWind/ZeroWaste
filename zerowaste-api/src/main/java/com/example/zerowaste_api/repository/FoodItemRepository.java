package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    @Query("SELECT f FROM FoodItem f WHERE f.expiryDate BETWEEN :today AND :futureDate")
    List<FoodItem> findItemsExpiringBetween(LocalDate today, LocalDate futureDate);
}
