package com.wildlifebackend.wildlife.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN(1, "Admin"),
    OPENUSER(2, "Open User"),
    STUDENT(3, "Student");

    private final int key;
    private final String value;

    UserRole(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
