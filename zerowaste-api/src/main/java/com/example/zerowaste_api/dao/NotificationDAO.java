package com.example.zerowaste_api.dao;

import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.FoodItemErrorConstant;
import com.example.zerowaste_api.common.error.NotificationErrorConstant;
import com.example.zerowaste_api.common.error.UserErrorConstant;
import com.example.zerowaste_api.entity.Notification;
import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public List<Notification> findByUserId (Long userId, NotificationType notificationType){
        return notificationRepository.findByUserIdAndNotifType(userId, notificationType);
    }

    public List<Notification> findByUnread (Long userId){

        return notificationRepository.findByUnread(userId);
    }

    public Notification findById(Long notifId){
        return notificationRepository.findById(notifId).orElseThrow(()->new ServiceAppException(
                HttpStatus.BAD_REQUEST,
                NotificationErrorConstant.NOTIFICATION_NOT_FOUND));
    }

    public void delete(Long id) {
        if (Objects.isNull(id)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, FoodItemErrorConstant.FOOD_ITEM_NOT_FOUND);
        }
        notificationRepository.deleteById(id);
    }

    public void deleteByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, UserErrorConstant.USER_NOT_FOUND);
        }

        notificationRepository.deleteWholeNotifications(userId);
    }
}

