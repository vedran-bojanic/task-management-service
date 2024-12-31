package com.asee.taskmanagementservice.task.repository;

import com.asee.taskmanagementservice.task.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskManagementRepository extends JpaRepository<TaskEntity, Integer> {
}
