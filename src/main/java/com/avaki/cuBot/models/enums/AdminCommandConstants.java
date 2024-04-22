package com.avaki.cuBot.models.enums;

public enum AdminCommandConstants {
    MOVE_USER("/moveUser");

    private final String value;

    AdminCommandConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
