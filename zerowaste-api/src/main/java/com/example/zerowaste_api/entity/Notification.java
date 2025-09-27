package com.example.zerowaste_api.entity;

import com.example.zerowaste_api.common.BaseDomain;
import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification")

public class Notification extends BaseDomain {

    private BigInteger usersId;

    @Enumerated (EnumType.STRING)
    private Status status;

    private String message;

    private Boolean markAsRead;

    @Column(name="notification_type")
    private NotificationType notifType;
}
