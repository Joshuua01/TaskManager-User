package com.project.taskmanager.user.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final Validator validator;

    public UserTest() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Test
    void testGetAuthoritiesWhenUserHasDefaultUserRole() {
        //given
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .email("john.doe@example.com")
                .build();
        //when
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();

        //then
        assertThat(authorities.size())
                .isEqualTo(1);
        assertThat(authorities)
                .contains(new SimpleGrantedAuthority(Role.USER.name()));
    }

    @Test
    void testGetAuthoritiesWhenUserHasAdminRole() {
        //given
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .email("john.doe@example.com")
                .role(Role.ADMIN)
                .build();
        //when
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user.getAuthorities();

        //then
        assertThat(authorities.size())
                .isEqualTo(1);
        assertThat(authorities)
                .contains(new SimpleGrantedAuthority(Role.ADMIN.name()));
    }

    @Test
    void testGetUsername() {
        //given
        String email = "john.doe@example.com";
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .email(email)
                .build();

        //when
        String username = user.getUsername();

        //then
        assertThat(username)
                .isEqualTo(email);
    }

    @Test
    public void testUserCreateWhenNotNullValuesAreNull() {
        //given
        User user = new User();

        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        //then
        assertThat(violations).hasSize(4);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("firstName"));
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("First name can not be empty"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("lastName"));
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("Last name can not be empty"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("password"));
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("Password can not be empty"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("Email can not be empty"));
    }

    @Test
    public void testUserCreateWhenEmailIsNotValid() {
        //given
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .email("not-valid-email")
                .build();

        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        //then
        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));
        assertThat(violations).anyMatch(violation -> violation.getMessage().equals("Email should be valid"));
    }
}