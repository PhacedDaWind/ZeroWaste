package com.example.zerowaste_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExpiringItemResDTO {
    private Long id;
    private String foodName;
    private Long quantity;
    private String daysUntilExpiry; // e.g., "3 days" or "Expires Today"
}