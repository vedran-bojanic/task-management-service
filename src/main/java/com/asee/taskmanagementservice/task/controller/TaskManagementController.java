package com.asee.taskmanagementservice.task.controller;

import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.service.TaskManagementService;
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

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskRequest) {
        var task = taskManagementService.create(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Integer id) {
        var fetchedTask = taskManagementService.getTaskById(id);
        if (fetchedTask == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fetchedTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam(value = "userId", required = false) Integer userId) {
        List<TaskDTO> tasks = taskManagementService.getTasksByUserId(userId);
        if (tasks == null || tasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Integer id, @RequestBody @Valid TaskDTO taskRequest) {
        var updatedTask = taskManagementService.updateTaskById(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> deleteTaskById(@PathVariable Integer id) {
        taskManagementService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
