package com.asee.taskmanagementservice.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.asee.taskmanagementservice.registration.repository.UserRepository;
import com.asee.taskmanagementservice.task.model.Status;
import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.model.TaskEntity;
import com.asee.taskmanagementservice.task.repository.TaskManagementRepository;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

@SpringBootTest
@ExtendWith(InstancioExtension.class)
class TaskManagementServiceTest {

    @InjectMocks
    private TaskManagementServiceImpl taskManagementService;

    @Mock
    private TaskManagementRepository taskManagementRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @Description("Test a Task creation flow")
    void should_createTask_when_onSuccessSave() {
        // prepare data
        TaskDTO taskDTO = Instancio.of(TaskDTO.class)
            .set(field(TaskDTO::userId), 1)
            .set(field(TaskDTO::status), "CREATED")
            .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
            .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
            .set(field(TaskDTO::userId), null)
            .create();
        TaskEntity taskEntity = Instancio.of(TaskEntity.class)
            .set(field(TaskEntity::getId), taskDTO.id())
            .set(field(TaskEntity::getName), taskDTO.name())
            .set(field(TaskEntity::getDescription), taskDTO.description())
            .set(field(TaskEntity::getStatus), Status.fromString(taskDTO.status()))
            .set(field(TaskEntity::getCreatedOn), ZonedDateTime.parse(taskDTO.createdOn()))
            .set(field(TaskEntity::getDueDate), ZonedDateTime.parse(taskDTO.dueDate()))
            .set(field(TaskEntity::getUser), null)
            .create();

        // Mock behavior
        when(userRepository.findById(anyInt())).thenReturn(null);
        when(taskManagementRepository.save(any())).thenReturn(taskEntity);

        // call service
        var task = taskManagementService.create(taskDTO);

        // assert
        assertThat(task).isNotNull();
        assertThat(task.name()).isEqualTo(taskDTO.name());
        assertThat(task.description()).isEqualTo(taskDTO.description());
        assertThat(task.status()).isEqualTo(taskDTO.status());
        assertThat(task.createdOn()).isEqualTo(taskDTO.createdOn());
        assertThat(task.dueDate()).isEqualTo(taskDTO.dueDate());
    }

    @Test
    @Description("Test a Task fetch by id flow")
    void should_fetchTask_when_onSuccess() {
        // prepare data
        TaskDTO taskDTO = Instancio.of(TaskDTO.class)
            .set(field(TaskDTO::userId), 1)
            .set(field(TaskDTO::status), "CREATED")
            .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
            .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
            .set(field(TaskDTO::userId), null)
            .create();
        TaskEntity taskEntity = Instancio.of(TaskEntity.class)
            .set(field(TaskEntity::getId), taskDTO.id())
            .set(field(TaskEntity::getName), taskDTO.name())
            .set(field(TaskEntity::getDescription), taskDTO.description())
            .set(field(TaskEntity::getStatus), Status.fromString(taskDTO.status()))
            .set(field(TaskEntity::getCreatedOn), ZonedDateTime.parse(taskDTO.createdOn()))
            .set(field(TaskEntity::getDueDate), ZonedDateTime.parse(taskDTO.dueDate()))
            .set(field(TaskEntity::getUser), null)
            .create();

        // Mock behavior
        when(userRepository.findById(anyInt())).thenReturn(null);
        when(taskManagementRepository.save(any())).thenReturn(taskEntity);

        // call service
        var task = taskManagementService.create(taskDTO);

        // assert
        assertThat(task).isNotNull();
        assertThat(task.id()).isEqualTo(taskDTO.id());
        assertThat(task.name()).isEqualTo(taskDTO.name());
        assertThat(task.description()).isEqualTo(taskDTO.description());
        assertThat(task.status()).isEqualTo(taskDTO.status());
        assertThat(task.createdOn()).isEqualTo(taskDTO.createdOn());
        assertThat(task.dueDate()).isEqualTo(taskDTO.dueDate());
    }

    @Test
    @Description("Test for fetching all tasks with userId as null")
    void should_fetchAllTask_when_noUserIdSpecified() {
        // prepare data
        Integer userId = null;
        TaskEntity taskDTO1 = Instancio.of(TaskEntity.class).create();
        TaskEntity taskDTO2 = Instancio.of(TaskEntity.class).create();

        // Mock behavior
        when(taskManagementRepository.findAll()).thenReturn(List.of(taskDTO1, taskDTO2));

        // call service
        var task = taskManagementService.getTasksByUserId(userId);

        // assert and verify
        assertThat(task).hasSize(2);
        verify(taskManagementRepository, times(1)).findAll();
    }

    @Test
    @Description("Test for fetching all tasks with userId specified in the call")
    void should_returnTasks_when_userIdProvided() {
        // prepare data
        int userId = 1;
        TaskEntity taskDTO1 = Instancio.of(TaskEntity.class).create();
        TaskEntity taskDTO2 = Instancio.of(TaskEntity.class).create();

        // Mock behavior
        when(taskManagementRepository.findByUserId(userId)).thenReturn(List.of(taskDTO1, taskDTO2));

        // call service
        List<TaskDTO> actualTasks = taskManagementService.getTasksByUserId(userId);

        // assert and verify
        assertThat(actualTasks).isNotNull();
        verify(taskManagementRepository, times(1)).findByUserId(userId);
    }

    @Test
    void should_returnEmptyList_when_noTasksExist() {
        // Mock behavior
        when(taskManagementRepository.findAll()).thenReturn(Collections.emptyList());

        /// call service
        List<TaskDTO> actualTasks = taskManagementService.getTasksByUserId(null);

        // assert and verify
        assertThat(actualTasks).isEmpty();
        verify(taskManagementRepository, times(1)).findAll();
    }
}
