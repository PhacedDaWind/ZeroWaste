package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.dto.UserRegistrationReqDTO;
import com.example.zerowaste_api.dto.UserRegistrationResDTO;
import com.example.zerowaste_api.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/registration")
public class RegistrationController extends BaseController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseDTO<UserRegistrationResDTO> registerUser(@RequestBody UserRegistrationReqDTO registrationReqDTO) {
        return createResponse(HttpStatus.OK, registrationService.registerUser(registrationReqDTO));
    }
}
