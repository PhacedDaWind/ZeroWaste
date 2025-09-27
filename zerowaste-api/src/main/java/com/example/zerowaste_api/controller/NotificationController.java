package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping

public class NotificationController extends BaseController {

    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("")
    public ResponseDTO<NotificationResDTO> notification(@RequestParam NotificationType notificationType) {
        return createResponse(HttpStatus.OK, notificationService.notification(notificationType));
    }

}
