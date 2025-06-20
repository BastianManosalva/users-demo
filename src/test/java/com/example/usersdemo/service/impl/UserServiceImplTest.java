package com.example.usersdemo.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.utils.Utils;

public class UserServiceImplTest {

    private UserRepository userRepo;
    private Utils utils;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        utils = mock(Utils.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserServiceImpl(utils);
        userService.userRepo = userRepo;
        userService.passwordEncoder = passwordEncoder;

        String email = "test@gmail.cl";
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null));
    }

    @Test
    void findUser_userFound() {
        User user = new User();
        user.setId("123");
        user.setEmail("test@gmail.cl");

        when(userRepo.findById("123")).thenReturn(Optional.of(user));

        User result = userService.findUser("123");

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("test@gmail.cl", result.getEmail());
    }

    @Test
    void findUser_otherEmailExcepcion() {
        User user = new User();
        user.setId("123");
        user.setEmail("bad_email@gmail.cl");

        when(userRepo.findById("123")).thenReturn(Optional.of(user));

        ServiceException ex = assertThrows(ServiceException.class, () -> userService.findUser("123"));
        assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED.value()), ex.getCode());
    }

    @Test
    void createUser_userCreated() {
        UserRequest request = UserRequest.builder()
                .email("new_email@gmail.cl")
                .password("Pass123")
                .name("new user")
                .build();

        when(userRepo.findByEmail("new_email@gmail.cl")).thenReturn(null);
        when(passwordEncoder.encode("Pass123")).thenReturn("hashedPass");

        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setEmail("new_email@gmail.cl");
        savedUser.setPassword("hashedPass");
        savedUser.setName("new user");
        savedUser.setIsActive(true);
        savedUser.setCreatedAt(new Date());

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        RegisterUserResponse result = userService.createUser(request);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getIsActive());
    }

    @Test
    void deleteUser_userDeleted() {
        User user = new User();
        user.setId("123");
        user.setName("Test User");
        user.setPhones(new ArrayList<>());
        user.setEmail("test@gmail.cl");

        when(userRepo.findById("123")).thenReturn(Optional.of(user));

        DeleteUserResponse response = userService.deleteUser("123");

        verify(userRepo, times(1)).save(user);
        verify(userRepo, times(1)).deleteById("123");

        assertNotNull(response);
        assertEquals("123", response.getId());
        assertEquals("Test User", response.getName());
        assertNotNull(response.getDeletionDate());
        assertNotNull(response.getMessage());
    }

}
