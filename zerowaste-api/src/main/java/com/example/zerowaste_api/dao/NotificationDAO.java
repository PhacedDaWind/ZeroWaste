package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.entity.Notification;
import com.example.zerowaste_api.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationDAO {
    private final NotificationRepository notificationRepository;

    public NotificationDAO(NotificationRepository notificationRepository) {
        this.notificationRepository=notificationRepository;
    }


    public Notification save(Notification notification) {
        if (Objects.isNull(notification)) {
            return null;
        }
        return notificationRepository.save(notification);
    }
}
