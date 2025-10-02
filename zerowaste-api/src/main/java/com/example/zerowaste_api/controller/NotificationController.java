package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/notification")

public class NotificationController extends BaseController {

    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/list/{id}")
    public ResponseDTO<List<NotificationResDTO>> readNotificationList(@PathVariable Long id,
                                                                      @RequestParam(required = false) NotificationType notificationType) {
        return createResponse(HttpStatus.OK, notificationService.readNotificationList(id, notificationType));
    }

    @GetMapping("/unread/{id}")
    public ResponseDTO<List<NotificationResDTO>> unreadNotificationList(@PathVariable Long id){
        return createResponse(HttpStatus.OK, notificationService.unreadNotificationList(id));
    }
    @PutMapping("/update/{id}")
    public ResponseDTO<NotificationResDTO> markAsRead(@PathVariable Long id) {
        return createResponse(HttpStatus.OK, notificationService.markAsRead(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<String> deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
        return createResponse(HttpStatus.OK, "OK");
    }

    @DeleteMapping("/alldelete/{userId}")
    public ResponseDTO<String> deleteAllNotifications(@PathVariable Long userId) {
        notificationService.deleteAll(userId);
        return createResponse(HttpStatus.OK, "OK");
    }
    // for testing purposes
//    @PostMapping("/create")
//    public ResponseDTO<NotificationResDTO> readNotificationList(@RequestBody NotificationRequestDTO notificationRequestDTO) {
//        return createResponse(HttpStatus.OK, notificationService.createFoodItemNotification(notificationRequestDTO));
//    }

}
