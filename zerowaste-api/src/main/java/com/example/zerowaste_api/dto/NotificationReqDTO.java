package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.common.BaseDomain;
import com.example.zerowaste_api.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NotificationReqDTO {
    private Long id;

    private Date createdAt;

    private Long usersId;

    private String message;

    private Boolean markAsRead;

    private NotificationType notifType;

    private String itemName;

    private BigDecimal quantity;

    private Date expiryDate;

    private String meal;

}
