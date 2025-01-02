package com.asee.taskmanagementservice.task.model;

import com.asee.taskmanagementservice.task.exception.InvalidStatusException;
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
            .orElseThrow(() -> new InvalidStatusException("Invalid status value: " + value));
    }
}
