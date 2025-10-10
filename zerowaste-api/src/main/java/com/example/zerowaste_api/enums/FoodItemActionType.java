package com.example.zerowaste_api.enums;

import com.example.zerowaste_api.common.BaseEnumInterface;

public enum FoodItemActionType implements BaseEnumInterface {
    MARK_AS_USED("Mark as used"), PLAN_FOR_MEAL("Plan for meal"), FLAG_FOR_DONATION("Flag for donation");

    private String label;

    private FoodItemActionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}