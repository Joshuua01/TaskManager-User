package com.project.task.user.configuration;

import com.project.task.user.domain.Role;
import com.project.task.user.domain.User;
import com.project.task.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setEmail("admin@admin.admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
