package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class NotificationResDTO {
    private LocalDateTime createdAt;

    private BigInteger usersId;

    private Status status;

    private String message;

    private Boolean markAsRead;

}
