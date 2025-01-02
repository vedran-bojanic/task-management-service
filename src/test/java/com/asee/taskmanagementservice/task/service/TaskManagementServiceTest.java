package com.asee.taskmanagementservice.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.asee.taskmanagementservice.registration.model.UserEntity;
import com.asee.taskmanagementservice.registration.repository.UserRepository;
import com.asee.taskmanagementservice.task.exception.InvalidStatusException;
import com.asee.taskmanagementservice.task.exception.TaskNotFoundException;
import com.asee.taskmanagementservice.task.exception.UserNotFoundException;
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
    class Create {
        @Test
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
    class GetTaskById {
        @Test
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
        void should_returnNull_when_taskNotFound() {
            // prepare data
            Integer taskId = 1;

            /// Mock behavior
            when(taskManagementRepository.findById(taskId)).thenReturn(Optional.empty());

            // assert throws
            assertThrows(TaskNotFoundException.class, () -> taskManagementService.getTaskById(taskId));
        }
    }

    @Nested
    class GetTasksByUser {
        @Test
        void should_fetchAllTask_when_noUserIdSpecified() {
            // prepare data
            Integer userId = null;
            List<TaskEntity> tasks = Instancio.ofList(TaskEntity.class).size(2).create();

            // Mock behavior
            when(taskManagementRepository.findAll()).thenReturn(tasks);

            // call service
            var task = taskManagementService.getTasksByUserId(userId);

            // assert and verify
            assertThat(task).hasSize(2);
            verify(taskManagementRepository, times(0)).findByUserId(userId);
            verify(taskManagementRepository, times(1)).findAll();
        }

        @Test
        void should_returnTasks_when_userIdProvided() {
            // prepare data
            Integer userId = 1;
            List<TaskEntity> tasks = Instancio.ofList(TaskEntity.class).size(2).create();

            // Mock behavior
            when(taskManagementRepository.findByUserId(userId)).thenReturn(tasks);

            // call service
            List<TaskDTO> actualTasks = taskManagementService.getTasksByUserId(userId);

            // assert and verify
            assertThat(actualTasks).isNotNull();
            verify(taskManagementRepository, times(1)).findByUserId(userId);
            verify(taskManagementRepository, times(0)).findAll();
        }

        @Test
        void should_returnEmptyList_when_noTasksExist() {
            // prepare data
            Integer userId = null;

            // Mock behavior
            when(taskManagementRepository.findAll()).thenReturn(Collections.emptyList());

            // call service
            List<TaskDTO> actualTasks = taskManagementService.getTasksByUserId(userId);

            // assert and verify
            assertThat(actualTasks).isEmpty();
            verify(taskManagementRepository, times(0)).findByUserId(userId);
            verify(taskManagementRepository, times(1)).findAll();
        }
    }

    @Nested
    class GetTasksByStatus {
        @Test
        void should_returnTasks_when_statusProvided() {
            // prepare data
            String status = "CREATED";
            Status statusEnum = Status.CREATED;
            List<TaskEntity> tasks = Instancio.ofList(TaskEntity.class).size(2).create();

            // Mock behavior
            when(taskManagementRepository.findTaskEntityByStatus(statusEnum)).thenReturn(tasks);

            // call service
            List<TaskDTO> actualTasks = taskManagementService.getTasksByStatus(status);

            // assert and verify
            assertThat(actualTasks).hasSize(2);
            verify(taskManagementRepository, times(1)).findTaskEntityByStatus(statusEnum);
        }

        @Test
        void should_returnEmpty_when_noTaskWithStatusProvided() {
            // prepare data
            String status = "CREATED";
            Status statusEnum = Status.CREATED;
            List<TaskEntity> tasks = Instancio.ofList(TaskEntity.class).size(0).create();

            // Mock behavior
            when(taskManagementRepository.findTaskEntityByStatus(statusEnum)).thenReturn(tasks);

            // call service
            List<TaskDTO> actualTasks = taskManagementService.getTasksByStatus(status);

            // assert and verify
            assertThat(actualTasks).isEmpty();
            verify(taskManagementRepository, times(1)).findTaskEntityByStatus(statusEnum);
        }

        @Test
        void should_returnInvalidStatusException_when_invalidStatusProvided() {
            // prepare data
            String status = "XXXX";

            // assert throws
            assertThrows(InvalidStatusException.class, () -> taskManagementService.getTasksByStatus(status));
        }
    }

    @Nested
    class Update {
        @Test
        void should_returnUpdatedTaskDTO_when_taskFoundAndUpdated() {
            // prepare data
            TaskEntity existingTaskEntity = Instancio.of(TaskEntity.class)
                .set(field(TaskEntity::getStatus), Status.CREATED)
                .create();
            UserEntity userToUpdate = Instancio.of(UserEntity.class)
                .set(field(UserEntity::getId), 1)
                .set(field(UserEntity::getUsername), "username")
                .create();
            TaskDTO taskDTOToUpdate = new TaskDTO(
                existingTaskEntity.getId(),
                "Updated name",
                "Updated description",
                "OPEN",
                "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
                "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
                userToUpdate.getId(),
                userToUpdate.getUsername());

            // Prepare expected updated task entity
            TaskEntity updatedTaskEntity = Instancio.of(TaskEntity.class)
                .set(field(TaskEntity::getName), taskDTOToUpdate.name())
                .set(field(TaskEntity::getDescription), taskDTOToUpdate.description())
                .set(field(TaskEntity::getStatus), Status.OPEN)
                .set(field(TaskEntity::getUser), userToUpdate)
                .create();

            // Mock behavior
            when(taskManagementRepository.findById(anyInt())).thenReturn(Optional.of(existingTaskEntity));
            when(userRepository.findById(anyInt())).thenReturn(Optional.of(userToUpdate));
            when(taskManagementRepository.save(any(TaskEntity.class))).thenReturn(updatedTaskEntity);

            // call service
            TaskDTO result = taskManagementService.updateTaskById(existingTaskEntity.getId(), taskDTOToUpdate);

            // assert and verify
            assertThat(result).isNotNull();
            assertThat(taskDTOToUpdate.name()).isEqualTo(result.name());
            assertThat(taskDTOToUpdate.description()).isEqualTo(result.description());
            assertThat(taskDTOToUpdate.status()).isEqualTo(result.status());
            assertThat(taskDTOToUpdate.userId()).isEqualTo(result.userId());
            assertThat(taskDTOToUpdate.username()).isEqualTo(result.username());
        }

        @Test
        void should_throwTaskNotFoundException_when_taskNotFound() {
            // prepare data
            Integer userId = 1;
            TaskDTO taskDTOToUpdate = Instancio.of(TaskDTO.class).create();

            // Mock behavior
            when(taskManagementRepository.findById(anyInt())).thenThrow(new TaskNotFoundException("Task not found"));

            // assert throws
            assertThrows(TaskNotFoundException.class, () -> taskManagementService.updateTaskById(userId, taskDTOToUpdate));
        }

        @Test
        void should_throwUserNotFoundException_when_userNotFound() {
            // prepare data
            Integer userId = 1;
            TaskEntity existingTaskEntity = Instancio.of(TaskEntity.class)
                .set(field(UserEntity::getId), userId)
                .create();
            TaskDTO taskDTOToUpdate = new TaskDTO(
                userId,
                "Updated name",
                "Updated description",
                "OPEN",
                "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
                "2024-12-31T10:00:00+01:00[Europe/Zagreb]",
                1,
                null);

            // Mock behavior
            when(taskManagementRepository.findById(anyInt())).thenReturn(Optional.of(existingTaskEntity));
            when(userRepository.findById(anyInt())).thenThrow(new UserNotFoundException("User not found"));

            // assert throws
            assertThrows(UserNotFoundException.class, () -> taskManagementService.updateTaskById(userId, taskDTOToUpdate));
        }
    }

    @Nested
    class Delete {
        @Test
        void should_deleteTaskById_when_Success() {
            // prepare data
            Integer taskId = 1;

            // Mock behavior
            when(taskManagementRepository.existsById(taskId)).thenReturn(true);
            doNothing().when(taskManagementRepository).deleteById(taskId);

            taskManagementService.deleteTaskById(taskId);

            // verify
            verify(taskManagementRepository, times(1)).existsById(taskId);
            verify(taskManagementRepository, times(1)).deleteById(taskId);
        }

        @Test
        void should_throwTaskNotFoundException_when_deleteTaskById() {
            // prepare data
            Integer taskId = 99;

            // Mock behavior
            when(taskManagementRepository.existsById(taskId)).thenReturn(false);

            // assert throws and verify
            assertThrows(TaskNotFoundException.class, () -> taskManagementService.deleteTaskById(taskId));
            verify(taskManagementRepository, times(1)).existsById(taskId);
            verify(taskManagementRepository, never()).deleteById(any());
        }
    }
}
