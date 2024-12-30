package com.asee.taskmanagementservice.registration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asee.taskmanagementservice.registration.model.UserDTO;
import com.asee.taskmanagementservice.registration.model.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_returnStatusOK_when_registerUser() throws Exception {
        // prepare data
        UserEntity user = new UserEntity();
        user.setId(1000);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPassword("password");

        // call endpoint
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.notNullValue()));
    }
}
