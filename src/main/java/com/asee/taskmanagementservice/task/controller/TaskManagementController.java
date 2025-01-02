package com.asee.taskmanagementservice.task.controller;

import com.asee.taskmanagementservice.task.exception.TaskNotFoundException;
import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.service.TaskManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    @Operation(summary = "Create a task", description = "Create a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the task"),
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskRequest) {
        var task = taskManagementService.create(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Get task by ID", description = "Retrieve task details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the task"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Integer id) {
        var fetchedTask = taskManagementService.getTaskById(id);
        return ResponseEntity.ok(fetchedTask);
    }

    @Operation(summary = "Get all tasks or for given userId", description = "Retrieve a list of tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
        @ApiResponse(responseCode = "404", description = "No tasks found")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(value = "userId", required = false) Integer userId) {
        List<TaskDTO> tasks = taskManagementService.getTasksByUserId(userId);
        if (tasks == null || tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found");
        }
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by a status", description = "Retrieve task details by a status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the tasks"),
        @ApiResponse(responseCode = "404", description = "No tasks found for status"),
        @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(@PathVariable String status) {
        List<TaskDTO> tasks = taskManagementService.getTasksByStatus(status);
        if (tasks == null || tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found for status: " + status);
        }
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Update a task by user id", description = "Update a task by user id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the tasks"),
        @ApiResponse(responseCode = "404", description = "No task found"),
        @ApiResponse(responseCode = "404", description = "No user found"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Integer id, @RequestBody @Valid TaskDTO taskRequest) {
        var updatedTask = taskManagementService.updateTaskById(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Delete a task by user id", description = "Delete a task by user id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the tasks"),
        @ApiResponse(responseCode = "404", description = "No task found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> deleteTaskById(@PathVariable Integer id) {
        taskManagementService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
