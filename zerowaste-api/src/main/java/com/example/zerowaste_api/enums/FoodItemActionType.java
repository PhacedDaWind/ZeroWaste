package com.example.zerowaste_api.enums;

import com.example.zerowaste_api.common.BaseEnumInterface;

public enum FoodItemActionType implements BaseEnumInterface {
    ACTION_1("Action 1"), ACTION_2("Action 2");

    private String label;

    private FoodItemActionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}