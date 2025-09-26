package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.dto.UserRegistrationReqDTO;
import com.example.zerowaste_api.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping

public class NotificationController extends BaseController {

    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("")
    public ResponseDTO<NotificationResDTO> notificationUser(@RequestBody UserRegistrationReqDTO notificationResDTO) {
        return createResponse(HttpStatus.OK, notificationService.(notificationResDTO));
    }

}
