package com.asee.taskmanagementservice.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS", uniqueConstraints = {
    @UniqueConstraint(columnNames = "USERNAME"),
    @UniqueConstraint(columnNames = "EMAIL")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username should not be empty")
    @Column(name = "USERNAME", length = 256, nullable = false, unique = true)
    private String username;

    @Email(message = "E-mail is invalid")
    @Column(name = "EMAIL", length = 256, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password should not be empty")
    @Column(name = "PASSWORD", length = 256, nullable = false)
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(getId(), user.getId()) &&
            Objects.equals(getUsername(), user.getUsername()) &&
            Objects.equals(getEmail(), user.getEmail()) &&
            Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail(), getPassword());
    }
}
