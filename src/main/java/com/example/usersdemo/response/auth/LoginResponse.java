package com.example.usersdemo.response.auth;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private UUID id;
    private Date lastLogin;
    private String token;
    private boolean isActive;

}
