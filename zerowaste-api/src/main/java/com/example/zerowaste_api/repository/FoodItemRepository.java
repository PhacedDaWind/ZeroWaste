package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    @Query("SELECT f FROM FoodItem f WHERE f.expiryDate BETWEEN :today AND :futureDate")
    List<FoodItem> findItemsExpiringBetween(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);

    @Query("SELECT f FROM FoodItem f WHERE f.user.id = :userId " +
            "AND f.name = :name " +
            "AND f.expiryDate = :expiryDate " +
            "AND f.convertToDonation = :isDonation")
    Optional<FoodItem> findDuplicateItem(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("expiryDate") LocalDate expiryDate,
            @Param("isDonation") Boolean isDonation
    );

    List<FoodItem> findAllByUserIdAndConvertToDonationFalseAndExpiryDateBetweenOrderByExpiryDateAsc(
            Long userId,
            LocalDate today,
            LocalDate limitDate
    );
}
