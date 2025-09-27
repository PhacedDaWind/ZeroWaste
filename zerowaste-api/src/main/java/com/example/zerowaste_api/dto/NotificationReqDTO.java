package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class NotificationReqDTO {
    private NotificationType notifType;
}

