package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class NotificationResDTO {
    private Long id;

    private Date createdAt;

    private String username;

    private String message;

    private Boolean markAsRead;

    private NotificationType notifType;
}
