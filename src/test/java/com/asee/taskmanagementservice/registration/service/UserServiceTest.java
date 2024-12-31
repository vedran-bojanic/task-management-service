package com.asee.taskmanagementservice.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.asee.taskmanagementservice.registration.model.UserDTO;
import com.asee.taskmanagementservice.registration.model.UserEntity;
import com.asee.taskmanagementservice.registration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO;
    private UserEntity userEntity;

    @BeforeEach
    void init() {
        // Initialize DTO and Entity for the test
        userDTO = new UserDTO("username", "username@test.hr", "password");
        userEntity = UserEntity.builder()
            .id(1)
            .username(userDTO.getUsername())
            .email(userDTO.getEmail())
            .password("encodedPassword")
            .build();
    }

    @Test
    @Description("Test map request to user entity and return user")
    void should_saveUser_when_registerUserIsCalled() {
        // prepare data
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(userEntity);

        // call service
        var userId = userService.register(userDTO);

        // assert
        assertThat(userId).isNotNull();
    }

}
