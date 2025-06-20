package com.example.usersdemo.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;
import com.example.usersdemo.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService service;

    /**
     * 
     * @param request {@link UserRequest}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint to create a user account.")
    @PostMapping("/create")
    public ResponseEntity<RegisterUserResponse> createUser(
            @Valid @RequestBody UserRequest request) throws ServiceException {
        LOGGER.info("Initiating request to create account with email: {}.", request.getEmail());
        final RegisterUserResponse response = service.createUser(request);
        LOGGER.info("User account created successfully with email: {}.", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 
     * @param token {@link String}
     * @param id    {@link Long}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint to find a user by ID.")
    @GetMapping("/{user-id}")
    public ResponseEntity<User> findUser(
            @PathVariable(value = "user-id", required = true) String id) throws ServiceException {
        LOGGER.info("Initiating request to find user with id: {}.", id);
        final User user = service.findUser(id);
        LOGGER.info("User found successfully with id: {}.", id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /**
     * 
     * @param token   {@link String}
     * @param request {@link UserUpdateRequest}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint to update a user.")
    @PutMapping("/{user-id}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable(value = "user-id", required = true) String id,
            @RequestBody UpdateUserRequest request) throws ServiceException {
        LOGGER.info("Initiating request to update user with id: {}.", id);
        final UpdateUserResponse response = service.updateUser(id, request);
        LOGGER.info("User with id {} updated successfully.", id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 
     * @param id      {@link String}
     * @param request {@link ChangeUserStatusRequest}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint change a user status.")
    @PatchMapping("/{user-id}/change-status")
    public ResponseEntity<ChangeUserStatusResponse> changeUserStatus(
            @PathVariable(value = "user-id", required = true) String id,
            @RequestBody ChangeUserStatusRequest request) throws ServiceException {
        LOGGER.info("Initiating request to change user: {} status.", id);
        final ChangeUserStatusResponse response = service.changeUserStatus(id, request);
        LOGGER.info("User status changed successfully for user with id: {}.", id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 
     * @param id {@link String}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint to delete a user.")
    @DeleteMapping("/{user-id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(
            @PathVariable(value = "user-id", required = true) String id) throws ServiceException {
        LOGGER.info("Initiating request to delete user with id: {}.", id);
        final DeleteUserResponse response = service.deleteUser(id);
        LOGGER.info("User with id {} deleted successfully.", id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 
     * @param token {@link String}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("Utility endpoint to list all users.")
    @GetMapping("/list")
    public ResponseEntity<List<User>> listUsers() throws ServiceException {
        final List<User> userList = service.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

}
