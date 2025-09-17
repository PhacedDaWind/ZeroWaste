package com.example.zerowaste_api.enums;

import com.example.zerowaste_api.common.BaseEnumInterface;

public enum Status implements BaseEnumInterface {
    ACTIVE("Active"), INACTIVE("Inactive");

    private String label;

    private Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
