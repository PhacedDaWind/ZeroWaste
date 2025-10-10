package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.dto.BrowseFoodItemTuple;
import com.example.zerowaste_api.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BrowseFoodItemRepository extends JpaRepository<FoodItem,Long> {
    @Query("SELECT u.id as id, "
            +"e.id as usersId, "
            +"u.convertToDonation as convertToDonation, "
            +"u.category as category, "
            +"u.expiryDate as expiryDate,"
            +"u.storageLocation as storageLocation, "
            +"u.name as userName, "
            +"u.quantity as quantity,"
            +"u.pickupLocation as pickupLocation,"
            +"u.contactMethod as contactMethod "
            +"FROM FoodItem u "
            +"LEFT JOIN u.user e "
            +"WHERE (:usersId IS NULL OR e.id=:usersId ) AND "
            +"(:convertToDonation IS NULL OR u.convertToDonation = :convertToDonation) AND "
            +"(:category IS NULL OR u.category LIKE :category) AND  "
            +"(:expiryDate IS NULL OR u.expiryDate=:expiryDate) AND "
            +"(:storageLocation IS NULL OR u.storageLocation LIKE :storageLocation)")
    Page<BrowseFoodItemTuple> getBrowse(Pageable pageable,
                                        @Param("usersId")Long usersId,
                                        @Param("convertToDonation")Boolean convertToDonation,
                                        @Param("category")String category,
                                        @Param("expiryDate") LocalDate expiryDate,
                                        @Param("storageLocation")String storageLocation);
}
