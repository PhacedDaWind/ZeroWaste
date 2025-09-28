package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.entity.Notification;
import com.example.zerowaste_api.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
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

    public List<Notification> findbyUserId (BigInteger userId){
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> findbyUnread (BigInteger userId){
        return notificationRepository.findByUnread(userId);
    }

    public void markAsRead (BigInteger notificationId, BigInteger userId) {
        notificationRepository.changeToRead(notificationId, userId);
    }

    public void deleteByNotificationId(BigInteger notificationId) {
        notificationRepository.deleteByNotificationId(notificationId);
    }

    public void deleteByUserId(BigInteger userId) {
        notificationRepository.deleteWholeNotifications(userId);
    }
}
