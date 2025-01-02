package com.asee.taskmanagementservice.registration.service;

import com.asee.taskmanagementservice.registration.exception.UserAlreadyExistException;
import com.asee.taskmanagementservice.registration.model.UserDTO;
import com.asee.taskmanagementservice.registration.model.UserEntity;
import com.asee.taskmanagementservice.registration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Integer register(UserDTO userRequest) {
        log.info("register request: {}", userRequest);
        var user = UserEntity.builder()
            .username(userRequest.username())
            .email(userRequest.email())
            .password(passwordEncoder.encode(userRequest.password()))
            .build();

        // User has to have uniqe username and email
        if (userRepository.existsByUsernameOrEmail(userRequest.username(), userRequest.email())) {
            throw new UserAlreadyExistException("Username or email already taken!");
        } else {
            return userRepository.save(user).getId();
        }
    }

}
