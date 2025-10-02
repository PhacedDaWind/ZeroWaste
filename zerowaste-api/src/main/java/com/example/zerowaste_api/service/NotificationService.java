package com.example.zerowaste_api.service;


import com.example.zerowaste_api.common.ServiceAppException;
import com.example.zerowaste_api.common.error.FoodItemErrorConstant;
import com.example.zerowaste_api.converter.NotificationConverter;
import com.example.zerowaste_api.dao.NotificationDAO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.entity.Notification;
import com.example.zerowaste_api.enums.NotificationType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;
    private final NotificationConverter notificationConverter;

    public NotificationService(NotificationDAO notificationDAO, NotificationConverter notificationConverter) {
        this.notificationDAO = notificationDAO;
        this.notificationConverter = notificationConverter;
    }

    public List<NotificationResDTO> readNotificationList(Long id, NotificationType notificationType) {
        List<Notification> notificationList = notificationDAO.findByUserId(id, notificationType);
        return notificationList.stream().map(notif -> {
            return notificationConverter.toNotificationResDTO(notif);
        }).collect(Collectors.toList());
    }

    public List<NotificationResDTO> unreadNotificationList(Long id) {
        List<Notification> unreadList= notificationDAO.findByUnread(id);
        return unreadList.stream().map(notif->{
            return notificationConverter.toNotificationResDTO(notif);
        }).collect(Collectors.toList());
    }

    public NotificationResDTO markAsRead(Long id) {
        Notification notification= notificationDAO.findById(id);
        if(notification.getMarkAsRead()==false) {
            notification.setMarkAsRead(true);
            notificationDAO.save(notification);
        }
        return notificationConverter.toNotificationResDTO(notification);
    }

    public void delete(Long id){
        if ( Objects.isNull(id)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, FoodItemErrorConstant.FOOD_ITEM_NOT_FOUND);
        }
        notificationDAO.delete(id);
    }

    public void deleteAll(Long userId){
        if (Objects.isNull(userId)) {
            throw new ServiceAppException(HttpStatus.BAD_REQUEST, "User ID cannot be null");
        }
        notificationDAO.deleteByUserId(userId);
    }

//    public NotificationResDTO createFoodItemNotification(String itemName, Long quantity, Long userId) {
//        //new notifcation entity
//        Notification notification = new Notification();
//
//        //converter to update notif (need to switch case for diff notif type)
//        notificationConverter.toNotifcation();
//
//        //save entity
//        notificationDAO.save(notification);
//
//        //return response
//        return notificationConverter.toNotificationResDTO(notification)
//    }
}
