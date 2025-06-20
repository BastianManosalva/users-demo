package com.example.usersdemo.service;

import com.example.usersdemo.request.auth.LoginRequest;
import com.example.usersdemo.response.auth.LoginResponse;

public interface AuthService {

    /**
     * Authenticates an user.
     *
     * @param request {@link LoginRequest}
     * @return {@link LoginResponse}
     */
    public LoginResponse login(LoginRequest request);

}
