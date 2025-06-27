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
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;
import com.example.usersdemo.response.user.UserResponse;
import com.example.usersdemo.service.UserService;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    private UUID testUuid;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController();
        userController.userService = userService;

        testUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    void createUser_userCreated() {
        UserRequest request = UserRequest.builder().email("test@gmail.cl").password("pass").name("name").build();
        RegisterUserResponse response = new RegisterUserResponse();
        response.setId(testUuid);
        when(userService.createUser(any(UserRequest.class))).thenReturn(response);

        ResponseEntity<RegisterUserResponse> result = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).createUser(request);
    }

    @Test
    void findUser_userFound() {
        UserResponse user = new UserResponse();
        user.setId(testUuid);
        when(userService.searchUser(testUuid.toString())).thenReturn(user);

        ResponseEntity<UserResponse> result = userController.searchUser(testUuid.toString());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).searchUser(testUuid.toString());
    }

    @Test
    void updateUser_updatedUser() {
        UpdateUserRequest request = new UpdateUserRequest();
        UpdateUserResponse response = new UpdateUserResponse();
        response.setId(testUuid);
        when(userService.updateUser(eq(testUuid.toString()), any(UpdateUserRequest.class))).thenReturn(response);

        ResponseEntity<UpdateUserResponse> result = userController.updateUser(testUuid.toString(), request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).updateUser(testUuid.toString(), request);
    }

    @Test
    void changeUserStatus_userStatusChanged() {
        ChangeUserStatusRequest request = new ChangeUserStatusRequest();
        ChangeUserStatusResponse response = new ChangeUserStatusResponse();
        response.setId(testUuid);
        when(userService.changeUserStatus(eq(testUuid.toString()), any(ChangeUserStatusRequest.class)))
                .thenReturn(response);

        ResponseEntity<ChangeUserStatusResponse> result = userController.changeUserStatus(testUuid.toString(), request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).changeUserStatus(testUuid.toString(), request);
    }

    @Test
    void deleteUser_userDeleted() {
        DeleteUserResponse response = new DeleteUserResponse();
        response.setId(testUuid);
        when(userService.deleteUser(testUuid.toString())).thenReturn(response);

        ResponseEntity<DeleteUserResponse> result = userController.deleteUser(testUuid.toString());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).deleteUser(testUuid.toString());
    }

    @Test
    void listUsers_returnsUserList() {
        UserResponse user = new UserResponse();
        user.setId(testUuid);
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(user));

        ResponseEntity<CollectionModel<UserResponse>> result = userController.listUsers();

        List<UserResponse> users = result.getBody().getContent().stream().collect(Collectors.toList());
        assertEquals(1, users.size());
        assertEquals(testUuid, users.get(0).getId());
        verify(userService, times(1)).findAllUsers();
    }

}
