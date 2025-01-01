package com.asee.taskmanagementservice.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.asee.taskmanagementservice.registration.model.UserEntity;
import com.asee.taskmanagementservice.registration.repository.UserRepository;
import com.asee.taskmanagementservice.task.model.Status;
import com.asee.taskmanagementservice.task.model.TaskDTO;
import com.asee.taskmanagementservice.task.model.TaskEntity;
import com.asee.taskmanagementservice.task.repository.TaskManagementRepository;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class createTask {
        @Test
        @Description("Test a Task creation flow with available user")
        void should_createTask_when_onSuccessSave() {
            // prepare data
            UserEntity userEntity = Instancio.of(UserEntity.class).create();
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::userId), userEntity.getId())
                .set(field(TaskDTO::username), userEntity.getUsername())
                .create();
            TaskEntity taskEntity = Instancio.of(TaskEntity.class)
                .set(field(TaskEntity::getId), taskDTO.id())
                .set(field(TaskEntity::getName), taskDTO.name())
                .set(field(TaskEntity::getDescription), taskDTO.description())
                .set(field(TaskEntity::getStatus), Status.fromString(taskDTO.status()))
                .set(field(TaskEntity::getCreatedOn), ZonedDateTime.parse(taskDTO.createdOn()))
                .set(field(TaskEntity::getDueDate), ZonedDateTime.parse(taskDTO.dueDate()))
                .set(field(TaskEntity::getUser), userEntity)
                .create();

            // Mock behavior
            when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));
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
            assertThat(task.userId()).isEqualTo(taskDTO.userId());
            assertThat(task.username()).isEqualTo(taskDTO.username());
        }

        @Test
        @Description("Test a Task creation flow without available user")
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
            assertThat(task.userId()).isNull();
            assertThat(task.username()).isNull();
        }
    }

    @Nested
    class getTaskById {
        @Test
        @Description("Test fetch a task by id when task exist")
        void should_returnTaskDTO_when_taskFound() {
            // prepare data
            UserEntity userEntity = Instancio.of(UserEntity.class).create();
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                .set(field(TaskDTO::status), "CREATED")
                .set(field(TaskDTO::createdOn), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::dueDate), "2024-12-31T10:00:00+01:00[Europe/Zagreb]")
                .set(field(TaskDTO::userId), userEntity.getId())
                .set(field(TaskDTO::username), userEntity.getUsername())
                .create();
            TaskEntity taskEntity = Instancio.of(TaskEntity.class)
                .set(field(TaskEntity::getId), taskDTO.id())
                .set(field(TaskEntity::getName), taskDTO.name())
                .set(field(TaskEntity::getDescription), taskDTO.description())
                .set(field(TaskEntity::getStatus), Status.fromString(taskDTO.status()))
                .set(field(TaskEntity::getCreatedOn), ZonedDateTime.parse(taskDTO.createdOn()))
                .set(field(TaskEntity::getDueDate), ZonedDateTime.parse(taskDTO.dueDate()))
                .set(field(TaskEntity::getUser), userEntity)
                .create();

            // Mock behavior
            when(taskManagementRepository.findById(taskDTO.id())).thenReturn(Optional.of(taskEntity));

            // call service
            TaskDTO result = taskManagementService.getTaskById(taskDTO.id());

            // assert and verify
            assertThat(result).isNotNull();
            assertThat(taskDTO.name()).isEqualTo(result.name());
        }

        @Test
        @Description("Test fetch a task by id when task doesn't exist")
        void should_returnNull_when_taskNotFound() {
            // prepare data
            TaskDTO taskDTO = Instancio.of(TaskDTO.class)
                .set(field(TaskDTO::userId), 1)
                    .create();

            /// Mock behavior
            when(taskManagementRepository.findById(taskDTO.id())).thenReturn(Optional.empty());

            // call service
            TaskDTO result = taskManagementService.getTaskById(taskDTO.id());

            // assert
            assertThat(result).isNull();
        }
    }

    @Nested
    class getTasksByUserId {
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
            verify(taskManagementRepository, times(0)).findByUserId(userId);
            verify(taskManagementRepository, times(1)).findAll();
        }

        @Test
        @Description("Test for fetching all tasks with userId specified in the call")
        void should_returnTasks_when_userIdProvided() {
            // prepare data
            Integer userId = 1;
            TaskEntity taskDTO1 = Instancio.of(TaskEntity.class).create();
            TaskEntity taskDTO2 = Instancio.of(TaskEntity.class).create();

            // Mock behavior
            when(taskManagementRepository.findByUserId(userId)).thenReturn(List.of(taskDTO1, taskDTO2));

            // call service
            List<TaskDTO> actualTasks = taskManagementService.getTasksByUserId(userId);

            // assert and verify
            assertThat(actualTasks).isNotNull();
            verify(taskManagementRepository, times(1)).findByUserId(userId);
            verify(taskManagementRepository, times(0)).findAll();
        }

        @Test
        @Description("Test for fetching all tasks when no task exist")
        void should_returnEmptyList_when_noTasksExist() {
            // prepare data
            Integer userId = null;

            // Mock behavior
            when(taskManagementRepository.findAll()).thenReturn(Collections.emptyList());

            /// call service
            List<TaskDTO> actualTasks = taskManagementService.getTasksByUserId(userId);

            // assert and verify
            assertThat(actualTasks).isEmpty();
            verify(taskManagementRepository, times(0)).findByUserId(userId);
            verify(taskManagementRepository, times(1)).findAll();
        }
    }

}
