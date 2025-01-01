package com.asee.taskmanagementservice.task.model;

import java.util.Arrays;

public enum Status {
    CREATED,
    OPEN,
    IN_PROGRESS,
    COMPLETED;

    public static Status fromString(String value) {
        return Arrays.stream(Status.values())
            .filter(status -> status.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid status value: " + value));
    }
}
