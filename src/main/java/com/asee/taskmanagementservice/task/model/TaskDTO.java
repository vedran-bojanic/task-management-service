package com.asee.taskmanagementservice.task.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Details about a task")
public record TaskDTO (
    @Schema(description = "Unique ID of the task", example = "1")
    Integer id,
    @Schema(description = "Name of the task", example = "Create a documentation")
    String name,
    @Schema(description = "Description of a task", example = "Documentation has to contain...")
    String description,
    @Schema(description = "Status of the task", example = "CREATED")
    String status,
    @Schema(description = "Date of creation of a task", example = "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
    String createdOn,
    @Schema(description = "Due date of a task", example = "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
    String dueDate,
    @Schema(description = "User that a task is assigned to", example = "1")
    Integer userId,
    @Schema(description = "Username of a user", example = "username")
    String username
) {

}

