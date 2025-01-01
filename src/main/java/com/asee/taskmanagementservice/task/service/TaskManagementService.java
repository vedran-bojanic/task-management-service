package com.asee.taskmanagementservice.task.service;

import com.asee.taskmanagementservice.task.model.TaskDTO;

public interface TaskManagementService {
    TaskDTO create(TaskDTO task);
    TaskDTO getTaskById(Integer id);
}
