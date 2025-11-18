package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.FoodItemErrorConstant;
import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.FoodItemDAO;
import com.example.zerowaste_api.dto.ExpiringItemResDTO;
import com.example.zerowaste_api.dto.FoodItemReqDTO;
import com.example.zerowaste_api.dto.FoodItemResDTO;
import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.NotificationType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    public FoodItemResDTO create(FoodItemReqDTO foodItemReqDTO) {
//        FoodItem foodItem = new FoodItem();
//        foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
//        if (foodItemReqDTO.getConvertToDonation().equals(Boolean.TRUE)) {
//            notificationService.create(NotificationType.DONATION_POSTED, foodItem.getUser().getId(), foodItem.getName(), foodItem.getQuantity(), foodItem.getExpiryDate(), null);
//        }
//        foodItemDAO.save(foodItem);
//        return foodItemConverter.toFoodItemResDTO(foodItem);
//    }

    @Transactional
    public FoodItemResDTO create(FoodItemReqDTO foodItemReqDTO) {
        // 1. Basic Validation
        if (Boolean.TRUE.equals(foodItemReqDTO.getConvertToDonation())) {
            Long totalQty = foodItemReqDTO.getQuantity();
            Long donateQty = foodItemReqDTO.getDonationQuantity();

            // Ensure donation quantity is valid
            if (donateQty == null || donateQty <= 0) {
                throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Donation quantity must be specified.");
            }
            if (donateQty > totalQty) {
                throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Donation quantity cannot exceed total quantity.");
            }

            // CASE A: SPLIT (Partial Donation)
            if (donateQty < totalQty) {
                // 1. Create Personal Item (Remainder)
                FoodItem personalItem = new FoodItem();
                personalItem = foodItemConverter.toFoodItem(foodItemReqDTO, personalItem);
                personalItem.setQuantity(totalQty - donateQty); // Set reduced quantity
                personalItem.setConvertToDonation(false);       // Ensure this is false
                foodItemDAO.save(personalItem);

                // 2. Create Donation Item
                FoodItem donationItem = new FoodItem();
                donationItem = foodItemConverter.toFoodItem(foodItemReqDTO, donationItem); // Copy same details
                donationItem.setQuantity(donateQty);            // Set donation quantity
                donationItem.setConvertToDonation(true);        // Set to true
                donationItem.setReservedQuantity(0L);           // No reservations on new donations

                foodItemDAO.save(donationItem);

                // Send Notification for the donation
                notificationService.create(NotificationType.DONATION_POSTED,
                        donationItem.getUser().getId(),
                        donationItem.getName(),
                        donationItem.getQuantity(),
                        donationItem.getExpiryDate(),
                        null
                );

                // Return the PERSONAL item so the UI updates the "My Inventory" list correctly
                return foodItemConverter.toFoodItemResDTO(personalItem);
            }
            // CASE B: FULL DONATION (Everything is donated)
            else {
                FoodItem donationItem = new FoodItem();
                donationItem = foodItemConverter.toFoodItem(foodItemReqDTO, donationItem);
                donationItem.setConvertToDonation(true);
                // quantity is already set by converter from DTO

                foodItemDAO.save(donationItem);

                notificationService.create(NotificationType.DONATION_POSTED,
                        donationItem.getUser().getId(),
                        donationItem.getName(),
                        donationItem.getQuantity(),
                        donationItem.getExpiryDate(),
                        null
                );

                return foodItemConverter.toFoodItemResDTO(donationItem);
            }

        } else {
            // CASE C: STANDARD CREATE (No Donation)
            FoodItem foodItem = new FoodItem();
            foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
            // Ensure donation flag is false just in case
            foodItem.setConvertToDonation(false);

            foodItemDAO.save(foodItem);
            return foodItemConverter.toFoodItemResDTO(foodItem);
        }
    }

//    public FoodItemResDTO update(Long id, FoodItemReqDTO foodItemReqDTO) {
//        FoodItem foodItem = foodItemDAO.findById(id);
//        if (foodItemReqDTO.getConvertToDonation().equals(Boolean.TRUE) && !foodItem.getConvertToDonation().equals(foodItemReqDTO.getConvertToDonation())) {
//            notificationService.create(NotificationType.DONATION_POSTED, foodItem.getUser().getId(), foodItem.getName(), foodItem.getQuantity(), foodItem.getExpiryDate(), null);
//        }
//        foodItem = foodItemConverter.toFoodItem(foodItemReqDTO, foodItem);
//        foodItemDAO.save(foodItem);
//        return foodItemConverter.toFoodItemResDTO(foodItem);
//    }

    @Transactional
    public FoodItemResDTO update(Long id, FoodItemReqDTO reqDTO) {
        FoodItem existingItem = foodItemDAO.findById(id);

        // LOGIC 1: Converting Personal Item TO Donation
        if (Boolean.TRUE.equals(reqDTO.getConvertToDonation()) && !Boolean.TRUE.equals(existingItem.getConvertToDonation())) {
            return handleConvertToDonation(existingItem, reqDTO);
        }
        // LOGIC 2: Reclaiming Donation Item BACK TO Personal
        else if (Boolean.FALSE.equals(reqDTO.getConvertToDonation()) && Boolean.TRUE.equals(existingItem.getConvertToDonation())) {
            return handleReclaimFromDonation(existingItem, reqDTO);
        }

        // LOGIC 3: Standard Update
        existingItem = foodItemConverter.toFoodItem(reqDTO, existingItem);
        foodItemDAO.save(existingItem);
        return foodItemConverter.toFoodItemResDTO(existingItem);
    }

    private FoodItemResDTO handleConvertToDonation(FoodItem existingItem, FoodItemReqDTO reqDTO) {

        // STEP 1: Determine the "New Total Quantity"
        // If user sent a new quantity in DTO, use it. Otherwise, use what's in DB.
        Long newTotalQuantity = (reqDTO.getQuantity() != null)
                ? reqDTO.getQuantity()
                : existingItem.getQuantity();

        Long reservedQty = reqDTO.getReservedQuantity();

        if (newTotalQuantity < reservedQty) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Cannot reduce quantity below the amount currently reserved in meal plans.");
        }

        // Check if fully reserved (and no room to donate)
        if (Objects.equals(reservedQty, newTotalQuantity)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Cannot donate item. It is fully reserved in Meal Plans.");
        }

        Long donationQty = reqDTO.getDonationQuantity();

        if (donationQty == null || donationQty <= 0) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Donation quantity must be specified.");
        }

        // STEP 3: The Core Check
        // Reserved (DB) + Donation (DTO) <= New Total (DTO or DB)
        if ((reservedQty + donationQty) <= newTotalQuantity) {

            // --- SPLIT RECORD LOGIC ---

            // Record 1 (Existing Personal):
            // Update this to be the (New Total - Donation Amount).
            existingItem = foodItemConverter.toFoodItem(reqDTO, existingItem);
            existingItem.setQuantity(newTotalQuantity - donationQty);
            existingItem.setConvertToDonation(false);

            foodItemDAO.save(existingItem);

            // Record 2 (New Donation):
            FoodItem donationItem = new FoodItem();

            // Copy details from the UPDATED existing item (so names/expiry match)
            foodItemConverter.copyFoodItem(donationItem, existingItem);

            donationItem.setQuantity(donationQty);
            donationItem.setConvertToDonation(true);
            donationItem.setReservedQuantity(0L);
            donationItem.setId(null); // Ensure new ID

            foodItemDAO.save(donationItem);

            notificationService.create(NotificationType.DONATION_POSTED, donationItem.getUser().getId(), donationItem.getName(), donationItem.getQuantity(), donationItem.getExpiryDate(), null);

            return foodItemConverter.toFoodItemResDTO(donationItem);
        } else {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "Insufficient quantity. Reserved + Donation amount exceeds total quantity.");
        }
    }

    private FoodItemResDTO handleReclaimFromDonation(FoodItem donationItem, FoodItemReqDTO reqDTO) {
        // Check if a personal item with the same details already exists to merge into.
        Optional<FoodItem> existingPersonalItemOpt = foodItemDAO.findDuplicateItem(
                donationItem.getUser().getId(),
                donationItem.getName(),
                donationItem.getExpiryDate(),
                false // Looking for PERSONAL item (not donation)
        );

        if (existingPersonalItemOpt.isPresent()) {
            // MERGE: Add quantity back to existing personal item
            FoodItem personalItem = existingPersonalItemOpt.get();
            personalItem.setQuantity(personalItem.getQuantity() + donationItem.getQuantity());

            foodItemDAO.save(personalItem);
            foodItemDAO.delete(donationItem.getId()); // Delete the donation record

            return foodItemConverter.toFoodItemResDTO(personalItem);
        } else {
            // CREATE NEW/UPDATE: Just switch flag to false
            donationItem.setConvertToDonation(false);
            foodItemDAO.save(donationItem);
            return foodItemConverter.toFoodItemResDTO(donationItem);
        }
    }

    public void delete(Long id) {
        if ( Objects.isNull(id)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, FoodItemErrorConstant.FOOD_ITEM_NOT_FOUND);
        }
        foodItemDAO.delete(id);
    }

    public List<ExpiringItemResDTO> getExpiringItemsList(Long userId) {

        LocalDate today = LocalDate.now();
        // Calculate the date that is exactly 3 days from now.
        // This makes the range today (0 days) up to 3 days from now (inclusive).
        LocalDate expiryLimitDate = today.plusDays(3);

        // 1. Fetch only items expiring within the next 3 days
        List<FoodItem> foodItems = foodItemDAO.findAllByUserIdAndConvertToDonationFalseAndExpiryDateBetweenOrderByExpiryDateAsc(
                userId,
                today,
                expiryLimitDate
        );

        // 2. Map the filtered items and calculate the expiry string
        return foodItems.stream()
                .map(item -> {
                    long days = ChronoUnit.DAYS.between(today, item.getExpiryDate());
                    String expiryString = calculateExpiryString(days); // Reusing the helper function

                    return ExpiringItemResDTO.builder()
                            .id(item.getId())
                            .foodName(item.getName())
                            .quantity(item.getQuantity())
                            .daysUntilExpiry(expiryString)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Helper function remains the same
    private String calculateExpiryString(long days) {
        if (days == 0) {
            return "Expires Today";
        } else if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "");
        } else { // Handle expired items that somehow still met the criteria (e.g., expired today)
            long daysAgo = Math.abs(days);
            return "Expired " + daysAgo + " day" + (daysAgo > 1 ? "s" : "") + " ago";
        }
    }
}
