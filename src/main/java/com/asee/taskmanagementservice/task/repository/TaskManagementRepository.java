package com.asee.taskmanagementservice.task.repository;

import com.asee.taskmanagementservice.task.model.Status;
import com.asee.taskmanagementservice.task.model.TaskEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskManagementRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findByUserId(Integer userId);
    List<TaskEntity> findTaskEntityByStatus(Status status);
}
