package com.example.zerowaste_api.converter;


import com.example.zerowaste_api.dto.NotificationReqDTO;
import com.example.zerowaste_api.dto.NotificationResDTO;
import com.example.zerowaste_api.entity.Notification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationConverter {

    public NotificationResDTO toNotification(Notification notification) {
        if(Objects.isNull(notification)){
            return null;
        }
        NotificationResDTO response= new NotificationResDTO();
        response.setUsersId(notification.getUsersId());
        response.setStatus(notification.getStatus());
        response.setMessage(notification.getMessage());
        response.setMarkAsRead(notification.getMarkAsRead());
        return response;
    }

}
