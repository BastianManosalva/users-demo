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

import com.example.usersdemo.request.auth.LoginRequest;
import com.example.usersdemo.response.auth.LoginResponse;
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

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.cl");
        request.setPassword("secretPass");

        LoginResponse mockResponse = new LoginResponse();
        mockResponse.setToken("token");
        mockResponse.setActive(true);

        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        ResponseEntity<LoginResponse> responseEntity = authController.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("token", responseEntity.getBody().getToken());
        assertTrue(responseEntity.getBody().isActive());

    }
}
