package com.asee.taskmanagementservice.task.model;

import com.asee.taskmanagementservice.registration.model.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TASKS")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Task name should not be empty")
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @NotBlank(message = "Task description should not be empty")
    @Column(name = "DESCRIPTION", length = 256, nullable = false)
    private String description;

    @NotNull(message = "Task status should not be null")
    @Column(name = "STATUS", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Task's create date should not be null")
    @Column(name = "CREATED_ON", nullable = false)
    private ZonedDateTime createdOn;

    @NotNull(message = "Task's due date should not be null")
    @Column(name = "DUE_DATE", nullable = false)
    private ZonedDateTime dueDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity task = (TaskEntity) o;
        return Objects.equals(getId(), task.getId()) &&
            Objects.equals(getName(), task.getName()) &&
            Objects.equals(getDescription(), task.getDescription()) &&
            Objects.equals(getStatus(), task.getStatus()) &&
            Objects.equals(getCreatedOn(), task.getCreatedOn()) &&
            Objects.equals(getDueDate(), task.getDueDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus(), getCreatedOn(), getDueDate());
    }
}
