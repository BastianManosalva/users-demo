package com.example.usersdemo.service;

import com.example.usersdemo.dto.auth.LoginRequestDTO;
import com.example.usersdemo.dto.auth.LoginResponseDTO;
import com.example.usersdemo.exception.ServiceException;

public interface AuthService {

    /**
     * Authenticates an user.
     *
     * @param request {@link LoginRequestDTO}
     * @return {@link LoginResponseDTO}
     */
    LoginResponseDTO login(LoginRequestDTO request) throws ServiceException;

}
