package com.asee.taskmanagementservice.health.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Collections;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Operation(summary = "Health endpoint", description = "Used only for testing purposes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check if the app is UP and running"),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok().body(Collections.singletonMap("status", "UP"));
    }

}
