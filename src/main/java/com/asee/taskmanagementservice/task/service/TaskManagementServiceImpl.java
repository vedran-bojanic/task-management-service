package com.asee.taskmanagementservice.task.service;

import com.asee.taskmanagementservice.registration.model.UserEntity;
import com.asee.taskmanagementservice.registration.repository.UserRepository;
import com.asee.taskmanagementservice.task.exception.TaskNotFoundException;
import com.asee.taskmanagementservice.task.exception.UserNotFoundException;
import com.asee.taskmanagementservice.task.model.Status;
import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.model.TaskEntity;
import com.asee.taskmanagementservice.task.repository.TaskManagementRepository;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskManagementRepository taskManagementRepository;
    private final UserRepository userRepository;

    @Override
    public TaskDTO create(TaskDTO task) {
        log.info("Create task with a request payload: {}", task);

        // check if the given user exist
        UserEntity user = null;
        if (task.userId() != null) {
            user = userRepository.findById(task.userId()).orElse(null);
        }

        var savedTask = taskManagementRepository.save(toEntity(task, user));
        return toDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Integer id) {
        log.info("Fetch task with a task id: {}", id);
        var optionalTaskEntity = taskManagementRepository.findById(id);
        if (optionalTaskEntity.isPresent()) { // check the task is present
            var task = optionalTaskEntity.get();
            return toDTO(task);
        }
        return null;
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Integer userId) {
        if (userId != null) {
            // Fetch tasks by userId
            return taskManagementRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
        } else {
            // Fetch all tasks
            return taskManagementRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        }
    }

    @Override
    public TaskDTO updateTaskById(Integer taskId, TaskDTO task) {
        log.info("Update task with a task id: {}", taskId);

        // Fetch if the task exist
        TaskEntity taskEntity = taskManagementRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        // Update fields
        taskEntity.setName(task.name());
        taskEntity.setDescription(task.description());
        taskEntity.setStatus(Status.fromString(task.status()));
        taskEntity.setDueDate(mapToZonedDateTime(task.dueDate()));

        if (task.userId() != null) {
            // Validate userId and set the UserEntity
            UserEntity user = userRepository.findById(task.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + task.userId()));
            taskEntity.setUser(user);
        }

        // Save and map the updated entity
        TaskEntity updatedTask = taskManagementRepository.save(taskEntity);
        return toDTO(updatedTask);
    }

    @Override
    public void deleteTaskById(Integer id) {
        log.info("Delete task with a task id: {}", id);

        if (!taskManagementRepository.existsById(id)) {
            log.warn("Task with ID {} does not exist.", id);
            throw new TaskNotFoundException("Task with ID " + id + " not found.");
        }

        taskManagementRepository.deleteById(id);
    }

    private TaskEntity toEntity(TaskDTO taskDTO, UserEntity user) {
        return TaskEntity.builder()
            .name(taskDTO.name())
            .description(taskDTO.description())
            .status(Status.fromString(taskDTO.status()))
            .createdOn(mapToZonedDateTime(taskDTO.createdOn()))
            .dueDate(mapToZonedDateTime(taskDTO.dueDate()))
            .user(user)
            .build();
    }

    private TaskDTO toDTO(TaskEntity taskEntity) {
        return new TaskDTO(
            taskEntity.getId(),
            taskEntity.getName(),
            taskEntity.getDescription(),
            taskEntity.getStatus().toString(),
            mapToString(taskEntity.getCreatedOn()),
            mapToString(taskEntity.getDueDate()),
            taskEntity.getUser() != null ? taskEntity.getUser().getId() : null,
            taskEntity.getUser() != null ? taskEntity.getUser().getUsername() : null
        );
    }

    private ZonedDateTime mapToZonedDateTime(String dateTimeString) {
        try {
            return ZonedDateTime.parse(dateTimeString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date-time format: " + dateTimeString, e);
        }
    }

    private String mapToString(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) : null;
    }
}
