package com.project.taskmanager.user.tools;

import com.project.taskmanager.user.domain.User;

import java.util.UUID;

public final class UserTestDataFactory {

    private UserTestDataFactory() {
    }

    public static User createValidUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john" + UUID.randomUUID() + "@example.com")
                .password("password")
                .build();
    }
}
