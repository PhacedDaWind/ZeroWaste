package com.example.zerowaste_api.service;


import com.example.zerowaste_api.converter.UsersConverter;
import com.example.zerowaste_api.dao.NotificationDAO;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationResDTO notificationResDTO;
    private final NotificationDAO notificationDAO;


}
