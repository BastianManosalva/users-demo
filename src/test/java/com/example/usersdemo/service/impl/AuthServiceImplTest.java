package com.example.usersdemo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.usersdemo.dto.auth.LoginRequestDTO;
import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
import com.example.usersdemo.utils.JwtUtil;
import com.example.usersdemo.utils.MessageUtils;

public class AuthServiceImplTest {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private AuthServiceImpl authService;
    private JwtUtil jwtUtil;

    private UUID testUuid;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);

        authService = new AuthServiceImpl(userRepo, passwordEncoder, jwtUtil);

        testUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void login_userNotFoundExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("not_valid_email@gmail.cl");
        request.setPassword("password");

        when(userRepo.findByEmail("not_valid_email@gmail.cl")).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class, () -> authService.login(request));
        assertEquals(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getCode());
        assertEquals(MessageUtils.USER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void login_passwordExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.cl");
        request.setPassword("bad_password");

        User user = new User();
        user.setId(testUuid);
        user.setEmail("test@gmail.cl");
        user.setPassword("hashed");
        user.setIsActive(true);

        when(userRepo.findByEmail("test@gmail.cl")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        ServiceException ex = assertThrows(ServiceException.class, () -> authService.login(request));
        assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED.value()), ex.getCode());
        assertEquals(MessageUtils.INVALID_PASSWORD, ex.getMessage());
    }

    @Test
    void login_userStaturExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.cl");
        request.setPassword("password");

        User user = new User();
        user.setId(testUuid);
        user.setEmail("test@gmail.cl");
        user.setPassword("hashed");
        user.setIsActive(false);

        when(userRepo.findByEmail("test@gmail.cl")).thenReturn(user);
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);

        ServiceException ex = assertThrows(ServiceException.class, () -> authService.login(request));
        assertEquals(String.valueOf(HttpStatus.FORBIDDEN.value()), ex.getCode());
        assertEquals(MessageUtils.USER_INACTIVE, ex.getMessage());
    }

}
