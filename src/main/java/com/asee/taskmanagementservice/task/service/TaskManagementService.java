package com.asee.taskmanagementservice.task.service;

import com.asee.taskmanagementservice.task.model.TaskDTO;
import java.util.List;

public interface TaskManagementService {
    TaskDTO create(TaskDTO task);
    TaskDTO getTaskById(Integer id);
    List<TaskDTO> getTasksByUserId(Integer userId);
    List<TaskDTO> getTasksByStatus(String status);
    TaskDTO updateTaskById(Integer id, TaskDTO task);
    void deleteTaskById(Integer id);
    TaskDTO assignTaskByUserId(Integer taskId, Integer userId);
}
