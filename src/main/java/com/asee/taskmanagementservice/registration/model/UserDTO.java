package com.asee.taskmanagementservice.registration.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserDTO(
    @Schema(description = "Username of a user", example = "username")
    String username,
    @Schema(description = "Email of a user", example = "email@email.com")
    String email,
    @Schema(description = "Password of a user", example = "xxxxxxx")
    String password
) {}
