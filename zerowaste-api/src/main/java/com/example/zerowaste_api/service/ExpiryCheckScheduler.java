package com.example.zerowaste_api.service;

import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.repository.FoodItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExpiryCheckScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ExpiryCheckScheduler.class);

    // Define "near expiry" as 3 days from now. You can change this value.
    private static final int EXPIRY_THRESHOLD_DAYS = 3;

    private final FoodItemRepository foodItemRepository;
    private final NotificationService notificationService; // Assuming your function is in this service

    public ExpiryCheckScheduler(FoodItemRepository foodItemRepository, NotificationService notificationService) {
        this.foodItemRepository = foodItemRepository;
        this.notificationService = notificationService;
    }

    //    @Scheduled(cron = "0 * * * * *") // testing purposes
    // 9am everyday
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void checkForSoonExpiringItems() {
        logger.info("Starting daily check for expiring food items...");

        LocalDate today = LocalDate.now();
        LocalDate expiryLimit = today.plusDays(EXPIRY_THRESHOLD_DAYS);

        // 1. Find the items
        List<FoodItem> expiringItems = foodItemRepository.findItemsExpiringBetween(today, expiryLimit);

        if (expiringItems.isEmpty()) {
            logger.info("No items found expiring in the next {} days.", EXPIRY_THRESHOLD_DAYS);
            return;
        }

        logger.info("Found {} items nearing expiry. Sending notifications...", expiringItems.size());

        // 2. Loop and send notifications for each item
        for (FoodItem item : expiringItems) {
            try {
                // Call the function as you requested
                // I am assuming your FoodItem entity has these fields
                notificationService.create(
                        NotificationType.FOOD_INVENTORY_ALERT, // Assuming this is an enum/constant
                        item.getUser().getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getExpiryDate(),
                        null
                );
            } catch (Exception e) {
                logger.error("Failed to send notification for item ID: {}", item.getId(), e);
            }
        }

        logger.info("Daily expiry check complete.");
    }
}