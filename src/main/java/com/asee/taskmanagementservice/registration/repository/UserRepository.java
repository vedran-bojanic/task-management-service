package com.asee.taskmanagementservice.registration.repository;

import com.asee.taskmanagementservice.registration.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> { }
