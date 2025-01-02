package com.asee.taskmanagementservice.registration.controller;

import com.asee.taskmanagementservice.registration.model.UserDTO;
import com.asee.taskmanagementservice.registration.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a user", description = "Create a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully with a User ID"),
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userRequest) {
        var userId = userService.register(userRequest);
        return ResponseEntity.ok("User registered successfully with a User ID: " + userId);
    }

}
