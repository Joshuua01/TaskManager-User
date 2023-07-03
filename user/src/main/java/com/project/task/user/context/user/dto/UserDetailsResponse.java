package com.project.task.user.context.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
