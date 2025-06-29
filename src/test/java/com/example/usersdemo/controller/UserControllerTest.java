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

import com.example.usersdemo.dto.user.ChangeUserStatusRequestDTO;
import com.example.usersdemo.dto.user.ChangeUserStatusResponseDTO;
import com.example.usersdemo.dto.user.DeleteUserResponseDTO;
import com.example.usersdemo.dto.user.RegisterUserResponseDTO;
import com.example.usersdemo.dto.user.UpdateUserRequestDTO;
import com.example.usersdemo.dto.user.UpdateUserResponseDTO;
import com.example.usersdemo.dto.user.UserRequestDTO;
import com.example.usersdemo.dto.user.UserResponseDTO;
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
        UserRequestDTO request = UserRequestDTO.builder().email("test@gmail.cl").password("pass").name("name").build();
        RegisterUserResponseDTO response = new RegisterUserResponseDTO();
        response.setId(testUuid);
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(response);

        ResponseEntity<RegisterUserResponseDTO> result = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).createUser(request);
    }

    @Test
    void findUser_userFound() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(testUuid);
        when(userService.searchUser(testUuid.toString())).thenReturn(user);

        ResponseEntity<UserResponseDTO> result = userController.searchUser(testUuid.toString());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).searchUser(testUuid.toString());
    }

    @Test
    void updateUser_updatedUser() {
        UpdateUserRequestDTO request = new UpdateUserRequestDTO();
        UpdateUserResponseDTO response = new UpdateUserResponseDTO();
        response.setId(testUuid);
        when(userService.updateUser(eq(testUuid.toString()), any(UpdateUserRequestDTO.class))).thenReturn(response);

        ResponseEntity<UpdateUserResponseDTO> result = userController.updateUser(testUuid.toString(), request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).updateUser(testUuid.toString(), request);
    }

    @Test
    void changeUserStatus_userStatusChanged() {
        ChangeUserStatusRequestDTO request = new ChangeUserStatusRequestDTO();
        ChangeUserStatusResponseDTO response = new ChangeUserStatusResponseDTO();
        response.setId(testUuid);
        when(userService.changeUserStatus(eq(testUuid.toString()), any(ChangeUserStatusRequestDTO.class)))
                .thenReturn(response);

        ResponseEntity<ChangeUserStatusResponseDTO> result = userController.changeUserStatus(testUuid.toString(),
                request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).changeUserStatus(testUuid.toString(), request);
    }

    @Test
    void deleteUser_userDeleted() {
        DeleteUserResponseDTO response = new DeleteUserResponseDTO();
        response.setId(testUuid);
        when(userService.deleteUser(testUuid.toString())).thenReturn(response);

        ResponseEntity<DeleteUserResponseDTO> result = userController.deleteUser(testUuid.toString());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUuid, result.getBody().getId());
        verify(userService, times(1)).deleteUser(testUuid.toString());
    }

    @Test
    void listUsers_returnsUserList() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(testUuid);
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(user));

        ResponseEntity<CollectionModel<UserResponseDTO>> result = userController.listUsers();

        List<UserResponseDTO> users = result.getBody().getContent().stream().collect(Collectors.toList());
        assertEquals(1, users.size());
        assertEquals(testUuid, users.get(0).getId());
        verify(userService, times(1)).findAllUsers();
    }

}
