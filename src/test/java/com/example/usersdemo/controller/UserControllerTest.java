package com.example.usersdemo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;
import com.example.usersdemo.service.UserService;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController();
        userController.service = userService;
    }

    @Test
    void createUser_userCreated() {
        UserRequest request = UserRequest.builder().email("test@gmail.cl").password("pass").name("name").build();
        RegisterUserResponse response = new RegisterUserResponse();
        response.setId("123");
        when(userService.createUser(any(UserRequest.class))).thenReturn(response);

        ResponseEntity<RegisterUserResponse> result = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("123", result.getBody().getId());
        verify(userService, times(1)).createUser(request);
    }

    @Test
    void findUser_userFound() {
        User user = new User();
        user.setId("123");
        when(userService.findUser("123")).thenReturn(user);

        ResponseEntity<User> result = userController.findUser("123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("123", result.getBody().getId());
        verify(userService, times(1)).findUser("123");
    }

    @Test
    void updateUser_updatedUser() {
        UpdateUserRequest request = new UpdateUserRequest();
        UpdateUserResponse response = new UpdateUserResponse();
        response.setId("123");
        when(userService.updateUser(eq("123"), any(UpdateUserRequest.class))).thenReturn(response);

        ResponseEntity<UpdateUserResponse> result = userController.updateUser("123", request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("123", result.getBody().getId());
        verify(userService, times(1)).updateUser("123", request);
    }

    @Test
    void changeUserStatus_userStatusChanged() {
        ChangeUserStatusRequest request = new ChangeUserStatusRequest();
        ChangeUserStatusResponse response = new ChangeUserStatusResponse();
        response.setId("123");
        when(userService.changeUserStatus(eq("123"), any(ChangeUserStatusRequest.class))).thenReturn(response);

        ResponseEntity<ChangeUserStatusResponse> result = userController.changeUserStatus("123", request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("123", result.getBody().getId());
        verify(userService, times(1)).changeUserStatus("123", request);
    }

    @Test
    void deleteUser_userDeleted() {
        DeleteUserResponse response = new DeleteUserResponse();
        response.setId("123");
        when(userService.deleteUser("123")).thenReturn(response);

        ResponseEntity<DeleteUserResponse> result = userController.deleteUser("123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("123", result.getBody().getId());
        verify(userService, times(1)).deleteUser("123");
    }

    @Test
    void listUsers_returnsUserList() {
        User user = new User();
        user.setId("123");
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<User>> result = userController.listUsers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("123", result.getBody().get(0).getId());
        verify(userService, times(1)).findAllUsers();
    }

}
