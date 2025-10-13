package com.example.zerowaste_api.service;


import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.FoodItemErrorConstant;
import com.example.zerowaste_api.common.error.NotificationErrorConstant;
import com.example.zerowaste_api.converter.NotificationConverter;
import com.example.zerowaste_api.dao.NotificationDAO;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.entity.Notification;
import com.example.zerowaste_api.entity.Users;
import com.example.zerowaste_api.enums.NotificationType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;
    private final NotificationConverter notificationConverter;
    private final UsersDAO usersDAO;

    public NotificationService(NotificationDAO notificationDAO, NotificationConverter notificationConverter, UsersDAO usersDAO) {
        this.notificationDAO = notificationDAO;
        this.notificationConverter = notificationConverter;
        this.usersDAO = usersDAO;
    }

    public List<NotificationResDTO> readNotificationList(Long id, NotificationType notificationType) {
        List<Notification> notificationList = notificationDAO.findByUserId(id, notificationType);
        return notificationList.stream().map(notif -> {
            return notificationConverter.toNotificationResDTO(notif);
        }).collect(Collectors.toList());
    }

    public List<NotificationResDTO> unreadNotificationList(Long id) {
        List<Notification> unreadList = notificationDAO.findByUnread(id);
        return unreadList.stream().map(notif -> {
            return notificationConverter.toNotificationResDTO(notif);
        }).collect(Collectors.toList());
    }

    public NotificationResDTO markAsRead(Long id) {
        Notification notification = notificationDAO.findById(id);
        if (notification.getMarkAsRead() == false) {
            notification.setMarkAsRead(true);
            notificationDAO.save(notification);
        }
        return notificationConverter.toNotificationResDTO(notification);
    }

    public void delete(Long id) {
        if (Objects.isNull(id)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, NotificationErrorConstant.NOTIFICATION_NOT_FOUND);
        }
        notificationDAO.delete(id);
    }

    public void deleteAll(Long userId) {
        if (Objects.isNull(userId)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
        }
        notificationDAO.deleteByUserId(userId);
    }

    public NotificationResDTO create(NotificationType notificationType,
                                     Long userId,
                                     List<String> itemName,
                                     List<Long> quantity,
                                     LocalDate expiryDate,
                                     String meal) {
        Users user = usersDAO.findById(userId);
        //new notifcation entity)
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setNotifType(notificationType);
        notification.setMarkAsRead(false);
        String message = null;
        //converter to update notif (need to switch case for diff notif type)
        switch (notificationType) {
            case FOOD_INVENTORY_ALERT:
                if (expiryDate == null) {
                    throw new ServiceAppException(HttpStatus.BAD_REQUEST,
                            NotificationErrorConstant.NOTIFICATION_INVALID_REQUEST);
                }
                Long diff = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
                if (diff >= 0 && diff <= 3) {
                    message = notificationType.format(itemName, expiryDate);
                    notification.setMessage(message);;
                } else {
                    return null;
                }
                break;

            case DONATION_POSTED:
                if (itemName == null || quantity == null || itemName.size() != quantity.size()) {
                    throw new ServiceAppException(HttpStatus.BAD_REQUEST,
                            NotificationErrorConstant.NOTIFICATION_INVALID_REQUEST);
                }
                message = notificationType.format(itemName, quantity);
                notification.setMessage(message);
                break;
            case DONATION_CLAIMED:
                message = notificationType.getLabel();
                notification.setMessage(message);
                break;
            case MEAL_REMINDER:
                message = notificationType.format(meal,expiryDate);
                notification.setMessage(message);;
                break;

            default:
                throw new IllegalArgumentException("Invalid Notification Type!!");
        }
        notificationDAO.save(notification);
        return notificationConverter.toNotificationResDTO(notification);
    }
}
