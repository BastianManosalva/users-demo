package com.example.usersdemo.request.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("secret");

        assertEquals("test@mail.com", request.getEmail());
        assertEquals("secret", request.getPassword());
    }
}
