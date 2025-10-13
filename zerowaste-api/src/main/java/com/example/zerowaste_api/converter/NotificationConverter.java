package com.example.zerowaste_api.converter;


import com.example.zerowaste_api.dto.NotificationReqDTO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.entity.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NotificationConverter {

    public NotificationResDTO toNotificationResDTO(Notification notification) {
        if(Objects.isNull(notification)){
            return null;
        }
        NotificationResDTO response= new NotificationResDTO();
        response.setId(notification.getId());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUsername(notification.getUser().getUsername());
        response.setUsername(notification.getUser().getUsername());
        response.setMessage(notification.getMessage());
        response.setMarkAsRead(notification.getMarkAsRead());
        response.setNotifType(notification.getNotifType());
        return response;
    }

}
