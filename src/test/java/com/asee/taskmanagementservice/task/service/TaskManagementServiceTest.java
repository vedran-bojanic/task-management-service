package com.asee.taskmanagementservice.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.asee.taskmanagementservice.registration.repository.UserRepository;
import com.asee.taskmanagementservice.task.model.Status;
import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.model.TaskEntity;
import com.asee.taskmanagementservice.task.repository.TaskManagementRepository;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

@SpringBootTest
class TaskManagementServiceTest {

    @InjectMocks
    private TaskManagementServiceImpl taskManagementService;

    @Mock
    private TaskManagementRepository taskManagementRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @Description("")
    void should_createTask_when_noErrorOccur() {
        // prepare data
        TaskDTO taskDTO = new TaskDTO(
            null,
            "name",
            "description",
            "CREATED",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
            null,
            null);
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1);
        taskEntity.setName("name");
        taskEntity.setDescription("description");
        taskEntity.setStatus(Status.CREATED);
        taskEntity.setCreatedOn(ZonedDateTime.parse(taskDTO.createdOn()));
        taskEntity.setDueDate(ZonedDateTime.parse(taskDTO.dueDate()));

        when(userRepository.findById(anyInt())).thenReturn(null);
        when(taskManagementRepository.save(any())).thenReturn(taskEntity);

        // call service
        var task = taskManagementService.create(taskDTO);

        // assert
        assertThat(task).isNotNull();
    }

}
