package com.asee.taskmanagementservice.task.model;

public record TaskDTO (
    String name,
    String description,
    String status,
    String createdOn,
    String dueDate,
    Integer userId
) {

}

