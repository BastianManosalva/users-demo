package com.example.usersdemo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
import com.example.usersdemo.request.auth.LoginRequest;
import com.example.usersdemo.response.auth.LoginResponse;
import com.example.usersdemo.service.AuthService;
import com.example.usersdemo.utils.JwtUtil;
import com.example.usersdemo.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail());
        if (user == null) {
            throw new ServiceException(String.valueOf(HttpStatus.NOT_FOUND.value()), MessageUtils.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ServiceException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), MessageUtils.INVALID_PASSWORD);
        }

        if (!user.getIsActive()) {
            throw new ServiceException(String.valueOf(HttpStatus.FORBIDDEN.value()), MessageUtils.USER_INACTIVE);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        Date now = new Date();
        user.setLastLogin(now);
        userRepo.save(user);
        user.setToken(token);

        return new LoginResponse(user.getId(), now, token, user.getIsActive());
    }

}
