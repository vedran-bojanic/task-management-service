package com.asee.taskmanagementservice.registration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "username should not be null")
    @Column(name = "USERNAME", length = 256, nullable = false)
    private String username;

    @Email(message = "email is invalid")
    @Column(name = "EMAIL", length = 256, nullable = false)
    private String email;

    @NotBlank(message = "password should not be null")
    @Column(name = "PASSWORD", length = 256, nullable = false)
    private String password;

}
