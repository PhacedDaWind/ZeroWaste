package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.common.BaseDomain;
import com.example.zerowaste_api.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NotificationReqDTO {

    private NotificationType notifType;

    private Long usersId;

    private String itemName;

    private Long quantity;

    private LocalDate expiryDate;

    private String meal;

}
