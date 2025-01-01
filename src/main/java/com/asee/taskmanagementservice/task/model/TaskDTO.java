package com.asee.taskmanagementservice.task.model;

public record TaskDTO (
    Integer id,
    String name,
    String description,
    String status,
    String createdOn,
    String dueDate,
    Integer userId,
    String username
) {

}

