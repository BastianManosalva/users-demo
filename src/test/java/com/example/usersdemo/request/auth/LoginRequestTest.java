package com.example.usersdemo.request.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.usersdemo.dto.auth.LoginRequestDTO;

public class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@mail.com");
        request.setPassword("secret");

        assertEquals("test@mail.com", request.getEmail());
        assertEquals("secret", request.getPassword());
    }
}
