package com.asee.taskmanagementservice.task.service;

import com.asee.taskmanagementservice.task.model.TaskDTO;
import java.util.List;

public interface TaskManagementService {
    TaskDTO create(TaskDTO task);
    TaskDTO getTaskById(Integer id);
    List<TaskDTO> getTasksByUserId(Integer userId);
    TaskDTO updateTaskById(Integer id, TaskDTO task);
}
