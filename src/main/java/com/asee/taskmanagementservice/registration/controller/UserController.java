package com.asee.taskmanagementservice.registration.controller;

import com.asee.taskmanagementservice.registration.model.UserDTO;
import com.asee.taskmanagementservice.registration.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userRequest) {
        var userId = userService.register(userRequest);
        return ResponseEntity.ok("User registered successfully with a User ID: " + userId);
    }

}
