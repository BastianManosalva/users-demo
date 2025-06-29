package com.example.usersdemo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.usersdemo.dto.auth.LoginRequestDTO;
import com.example.usersdemo.dto.auth.LoginResponseDTO;
import com.example.usersdemo.service.AuthService;

public class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController();
        authController.authService = authService;
    }

    @Test
    void login_retornaLoginResponseOK() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.cl");
        request.setPassword("secretPass");

        LoginResponseDTO mockResponse = new LoginResponseDTO();
        mockResponse.setToken("token");
        mockResponse.setActive(true);

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(mockResponse);

        ResponseEntity<LoginResponseDTO> responseEntity = authController.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("token", responseEntity.getBody().getToken());
        assertTrue(responseEntity.getBody().isActive());

    }
}
