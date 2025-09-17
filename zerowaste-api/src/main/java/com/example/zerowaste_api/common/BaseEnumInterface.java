package com.example.zerowaste_api.common;

public interface BaseEnumInterface {
    String getLabel();

    default <T> T getValue() {
        return null;
    }
}
