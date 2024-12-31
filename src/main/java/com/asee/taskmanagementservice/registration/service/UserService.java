package com.asee.taskmanagementservice.registration.service;

import com.asee.taskmanagementservice.registration.model.UserDTO;

public interface UserService {

    Integer register(UserDTO request);

}
