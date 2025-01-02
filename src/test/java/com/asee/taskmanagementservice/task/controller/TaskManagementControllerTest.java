package com.asee.taskmanagementservice.task.controller;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.asee.taskmanagementservice.task.exception.TaskNotFoundException;
import com.asee.taskmanagementservice.task.exception.UserNotFoundException;
import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.service.TaskManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(InstancioExtension.class)
class TaskManagementControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    private TaskManagementService taskManagementService;

    @Nested
    class Create {
        @Test
        void should_returnStatusOK_when_creatingTask() throws Exception {
            // prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .create();

            // Mock behavior
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
    }

    @Nested
    class GetTaskById {
        @Test
        void should_returnNotFound_when_fetchingTaskById() throws Exception {
            // prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .create();

            // Mock behavior
            when(taskManagementService.getTaskById(taskDTO.id())).thenThrow(new TaskNotFoundException("No tasks found."));

            // call endpoint
            mockMvc.perform(get("/tasks/" + taskDTO.id()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        void should_returnTaskDTO_when_fetchingTaskById() throws Exception {
            // prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::userId), null)
                .set(field(TaskDTO::username), null)
                .create();

            // Mock behavior
            when(taskManagementService.getTaskById(taskDTO.id())).thenReturn(taskDTO);

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

    @Nested
    class GetTasksByUser {
        @Test
        void should_returnTasks_when_fetchingByUserId() throws Exception {
            // prepare data
            List<TaskDTO> tasks = Instancio.ofList(TaskDTO.class).size(3).create();
            int userId = 1;

            // Mock behavior
            when(taskManagementService.getTasksByUserId(userId)).thenReturn(tasks);

            // call endpoint
            mockMvc.perform(get("/tasks")
                    .param("userId", String.valueOf(userId))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name", Matchers.is(tasks.get(0).name())))
                .andExpect(jsonPath("$[1].name", Matchers.is(tasks.get(1).name())))
                .andExpect(jsonPath("$[2].name", Matchers.is(tasks.get(2).name())));
        }

        @Test
        void should_returnNotFound_when_noTasksForUserId() throws Exception {
            // prepare data
            int userId = 1;

            // Mock behavior
            when(taskManagementService.getTasksByUserId(userId)).thenReturn(Collections.emptyList());

            // call endpoint
            mockMvc.perform(get("/tasks")
                    .param("userId", String.valueOf(userId))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        void should_returnNotFound_when_noTasksWithoutUserId() throws Exception {
            // Mock behavior
            when(taskManagementService.getTasksByUserId(null)).thenReturn(Collections.emptyList());

            // call endpoint
            mockMvc.perform(get("/tasks")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        void should_returnTasks_when_noUserIdProvided() throws Exception {
            // prepare data
            List<TaskDTO> tasks = Instancio.ofList(TaskDTO.class).size(2).create();

            // Mock behavior
            when(taskManagementService.getTasksByUserId(null)).thenReturn(tasks);

            // call endpoint
            mockMvc.perform(get("/tasks")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is(tasks.get(0).name())))
                .andExpect(jsonPath("$[1].name", Matchers.is(tasks.get(1).name())));
        }
    }

    @Nested
    class Update {
        @Test
        void should_returnStatusOK_when_updatingTask() throws Exception {
            // prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .create();

            // Mock behavior
            when(taskManagementService.updateTaskById(anyInt(), any(TaskDTO.class))).thenReturn(taskDTO);

            // call endpoint
            mockMvc.perform(MockMvcRequestBuilders.put("/tasks/" + taskDTO.id())
                    .content(objectMapper.writeValueAsString(taskDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
        void should_returnNotFound_when_taskNotFound() throws Exception {
            // Prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::id), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .create();

            // Mock behavior
            when(taskManagementService.updateTaskById(anyInt(), any(TaskDTO.class)))
                .thenThrow(new TaskNotFoundException("Task not found with ID: 1"));

            // Call endpoint
            mockMvc.perform(MockMvcRequestBuilders.put("/tasks/" + taskDTO.id())
                    .content(objectMapper.writeValueAsString(taskDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        void should_returnNotFound_when_userNotFound() throws Exception {
            // Prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::userId), 1)
                .create();

            // Mock behavior
            when(taskManagementService.updateTaskById(anyInt(), any(TaskDTO.class)))
                .thenThrow(new UserNotFoundException("User not found with ID: 1"));

            // Call endpoint
            mockMvc.perform(MockMvcRequestBuilders.put("/tasks/" + taskDTO.id())
                    .content(objectMapper.writeValueAsString(taskDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    class Delete {
        @Test
        void testDeleteTaskById() throws Exception {
            // Prepare data
            Integer taskId = 1;

            // Mock behavior
            doNothing().when(taskManagementService).deleteTaskById(taskId);

            // Call endpoint
            mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

            // Verify
            verify(taskManagementService, times(1)).deleteTaskById(taskId);
        }

        @Test
        void testDeleteTaskNotFound() throws Exception {
            // Prepare data
            Integer taskId = 99;

            // Mock behavior
            doThrow(new TaskNotFoundException("Task not found"))
                .when(taskManagementService).deleteTaskById(taskId);

            // Call endpoint
            mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
        }
    }
}
