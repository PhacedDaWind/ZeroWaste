package com.example.zerowaste_api.entity;

import com.example.zerowaste_api.common.BaseDomain;
import com.example.zerowaste_api.enums.NotificationType;
import com.example.zerowaste_api.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification")
public class Notification extends BaseDomain {

    private String message;

    private Boolean markAsRead;

    @Column(name="notification_type")
    @Enumerated (EnumType.STRING)
    private NotificationType notifType;

    @ManyToOne
    @JoinColumn(name="users_id")
    private Users user;
}
