package com.example.usersdemo.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;
import com.example.usersdemo.service.UserService;
import com.example.usersdemo.utils.JwtUtil;
import com.example.usersdemo.utils.MessageUtils;
import com.example.usersdemo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Utils utils;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User findUser(String id) {

        String emailFromJwt = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        var user = userRepo.findById(id).orElseThrow(() -> new ServiceException(
                String.valueOf(HttpStatus.NOT_FOUND.value()), MessageUtils.USER_DOES_NOT_EXIST));

        if (!user.getEmail().equals(emailFromJwt)) {
            LOGGER.warn("Unauthorized access attempt for user id: {} by email: {}", id, emailFromJwt);
            throw new ServiceException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    MessageUtils.UNAUTHORIZED_STATUS);
        }

        return user;
    }

    @Override
    @Transactional
    public RegisterUserResponse createUser(final UserRequest request) {

        utils.emailValidator(request.getEmail());
        utils.passwordValidator(request.getPassword());
        utils.existingEmailValidator(request.getEmail());

        Date date = new Date();

        final var user = new User();
        user.setId(Utils.generateID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user.setCreatedAt(date);
        user.setModified(date);
        user.setLastLogin(date);
        user.setPhones(request.getPhones());
        user.setToken(jwtUtil.generateToken(user.getEmail()));

        final var createdUser = userRepo.save(user);

        return RegisterUserResponse.builder()
                .id(createdUser.getId())
                .isActive(createdUser.getIsActive())
                .created(createdUser.getCreatedAt())
                .modified(createdUser.getModified())
                .lastLogin(createdUser.getLastLogin())
                .token(createdUser.getToken())
                .build();

    }

    @Override
    public UpdateUserResponse updateUser(final String id, final UpdateUserRequest request) {

        final var user = findUser(id);

        utils.emailValidator(request.getEmail());
        utils.passwordValidator(request.getPassword());
        utils.existingEmailValidator(request.getEmail());

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getPhones().clear();
        user.getPhones().addAll(request.getPhones());
        user.setModified(new Date());

        final var update = userRepo.save(user);

        final var updateUser = new UpdateUserResponse();
        updateUser.setId(update.getId());
        updateUser.setCreated(update.getCreatedAt());
        updateUser.setModified(update.getModified());
        updateUser.setLastLogin(update.getLastLogin());
        updateUser.setToken(update.getToken());
        updateUser.setIsActive(update.getIsActive());
        updateUser.setPhones(update.getPhones());
        updateUser.setMessage(MessageUtils.UPDATE_USER_MESSAGE);
        return updateUser;

    }

    @Override
    public ChangeUserStatusResponse changeUserStatus(String id, ChangeUserStatusRequest request) {

        final var user = findUser(id);

        if (request.isActive() && user.getIsActive()) {
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    MessageUtils.USER_ALREADY_ACTIVE);
        }

        if (!request.isActive() && !user.getIsActive()) {
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    MessageUtils.USER_ALREADY_INACTIVE);
        }

        user.setIsActive(request.isActive());
        user.setModified(new Date());

        final var updatedUser = userRepo.save(user);

        var response = new ChangeUserStatusResponse();
        response.setId(updatedUser.getId());
        response.setActive(updatedUser.getIsActive());
        return response;

    }

    @Override
    public DeleteUserResponse deleteUser(String id) {

        final var user = findUser(id);

        user.getPhones().clear();
        userRepo.save(user);

        userRepo.deleteById(id);

        final var deletedUser = new DeleteUserResponse();
        deletedUser.setId(user.getId());
        deletedUser.setName(user.getName());
        deletedUser.setDeletionDate(new Date());
        deletedUser.setMessage(MessageUtils.DELETE_USER_MESSAGE);

        return deletedUser;

    }

    @Override
    public List<User> findAllUsers() {

        return (List<User>) userRepo.findAll();

    }
}
