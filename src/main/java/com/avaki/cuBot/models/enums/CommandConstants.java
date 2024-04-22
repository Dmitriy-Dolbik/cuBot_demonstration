package com.avaki.cuBot.models.enums;

public enum CommandConstants {
    START("/start"),
    CU("/cu"),
    REGISTER("/register"),
    WIFI("/wifi");

    private final String value;

    CommandConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
