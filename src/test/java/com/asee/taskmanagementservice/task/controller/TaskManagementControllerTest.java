package com.asee.taskmanagementservice.task.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asee.taskmanagementservice.task.model.TaskDTO;
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
class TaskManagementControllerTest {

    @Test
    void should_returnStatusOK_when_creatingTask(
        @Autowired MockMvc mockMvc,
        @Autowired ObjectMapper objectMapper
    ) throws Exception {
        // prepare data
        TaskDTO taskDTO = new TaskDTO(
            "name",
            "description",
            "CREATED",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            null);

        // call endpoint
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", Matchers.notNullValue()))
            .andExpect(jsonPath("$.name", Matchers.is(taskDTO.name())))
            .andExpect(jsonPath("$.description", Matchers.is(taskDTO.description())))
            .andExpect(jsonPath("$.status", Matchers.is(taskDTO.status())))
            .andExpect(jsonPath("$.createdOn", Matchers.is(taskDTO.createdOn())))
            .andExpect(jsonPath("$.dueDate", Matchers.is(taskDTO.dueDate())));
    }

}
