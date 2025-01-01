package com.asee.taskmanagementservice.task.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.service.TaskManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskManagementControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    private TaskManagementService taskManagementService;

    @Test
    void should_returnStatusOK_when_creatingTask() throws Exception {
        // prepare data
        TaskDTO taskDTO = new TaskDTO(
            1,
            "name",
            "description",
            "CREATED",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            1,
            "testUser");

        // Mock the service response
        when(taskManagementService.create(taskDTO)).thenReturn(taskDTO);

        // call endpoint
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", Matchers.notNullValue()))
            .andExpect(jsonPath("$.id", Matchers.is(taskDTO.id())))
            .andExpect(jsonPath("$.name", Matchers.is(taskDTO.name())))
            .andExpect(jsonPath("$.description", Matchers.is(taskDTO.description())))
            .andExpect(jsonPath("$.status", Matchers.is(taskDTO.status())))
            .andExpect(jsonPath("$.createdOn", Matchers.is(taskDTO.createdOn())))
            .andExpect(jsonPath("$.dueDate", Matchers.is(taskDTO.dueDate())))
            .andExpect(jsonPath("$.userId", Matchers.is(taskDTO.userId())))
            .andExpect(jsonPath("$.username", Matchers.is(taskDTO.username())));
    }

    @Test
    void should_returnNotFound_when_fetchingTaskById() throws Exception {
        // prepare data
        TaskDTO taskDTO = new TaskDTO(
            1,
            "name",
            "description",
            "CREATED",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            null,
            null);

        // Mock the service response
        when(taskManagementService.getTaskById(taskDTO.id())).thenReturn(null);

        // call endpoint
        mockMvc.perform(get("/tasks/" + taskDTO.id()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void should_returnTaskDTO_when_fetchingTaskById() throws Exception {
        // prepare data
        TaskDTO taskDTO = new TaskDTO(
            1,
            "name",
            "description",
            "CREATED",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            null,
            null);

        // Mock the service response
        when(taskManagementService.getTaskById(1)).thenReturn(taskDTO);

        // call endpoint
        mockMvc.perform(get("/tasks/" + taskDTO.id()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.notNullValue()))
            .andExpect(jsonPath("$.name", Matchers.is(taskDTO.name())))
            .andExpect(jsonPath("$.description", Matchers.is(taskDTO.description())))
            .andExpect(jsonPath("$.status", Matchers.is(taskDTO.status())))
            .andExpect(jsonPath("$.createdOn", Matchers.is(taskDTO.createdOn())))
            .andExpect(jsonPath("$.dueDate", Matchers.is(taskDTO.dueDate())));
    }
}
