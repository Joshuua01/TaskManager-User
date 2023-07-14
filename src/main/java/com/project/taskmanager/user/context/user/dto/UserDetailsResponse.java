package com.project.taskmanager.user.context.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
