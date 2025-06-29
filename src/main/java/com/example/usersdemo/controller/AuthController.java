package com.example.usersdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usersdemo.dto.auth.LoginRequestDTO;
import com.example.usersdemo.dto.auth.LoginResponseDTO;
import com.example.usersdemo.service.AuthService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @ApiOperation("Endpoint to login.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LOGGER.info("Initiating login service.");
        final LoginResponseDTO response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
