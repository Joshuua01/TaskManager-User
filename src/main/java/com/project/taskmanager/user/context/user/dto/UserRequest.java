package com.project.taskmanager.user.context.user.dto;

import com.project.taskmanager.user.domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "First name can not be empty")
    private String firstName;
    @NotBlank(message = "Last name can not be empty")
    private String lastName;
    @NotBlank(message = "Email can not be empty")
    private String email;
    @NotBlank(message = "Password can not be empty")
    private String password;
    private Role role;
}
