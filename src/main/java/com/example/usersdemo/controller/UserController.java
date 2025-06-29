package com.example.usersdemo.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import com.example.usersdemo.dto.user.ChangeUserStatusRequestDTO;
import com.example.usersdemo.dto.user.ChangeUserStatusResponseDTO;
import com.example.usersdemo.dto.user.DeleteUserResponseDTO;
import com.example.usersdemo.dto.user.RegisterUserResponseDTO;
import com.example.usersdemo.dto.user.UpdateUserRequestDTO;
import com.example.usersdemo.dto.user.UpdateUserResponseDTO;
import com.example.usersdemo.dto.user.UserRequestDTO;
import com.example.usersdemo.dto.user.UserResponseDTO;
import com.example.usersdemo.service.UserService;

import io.swagger.annotations.ApiOperation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String UPDATE_REL = "update";
    private static final String DELETE_REL = "delete";
    private static final String USERS_REL = "users";

    @Autowired
    UserService userService;

    /**
     * 
     * @param request {@link UserRequest}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint to create a new user.")
    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO request) throws ServiceException {
        LOGGER.info("Initiating request to create account with email: {}.", request.getEmail());
        final RegisterUserResponseDTO response = userService.createUser(request);

        String id = response.getId().toString();
        response.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel(UPDATE_REL));
        response.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel(DELETE_REL));
        response.add(linkTo(methodOn(UserController.class).listUsers()).withRel(USERS_REL));

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
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> searchUser(@PathVariable String id) throws ServiceException {
        LOGGER.info("Initiating request to find user with id: {}.", id);
        final UserResponseDTO response = userService.searchUser(id);

        response.add(linkTo(methodOn(UserController.class).searchUser(id)).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel(UPDATE_REL));
        response.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel(DELETE_REL));
        response.add(linkTo(methodOn(UserController.class).listUsers()).withRel(USERS_REL));

        LOGGER.info("User found successfully with id: {}.", id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 
     * @param token   {@link String}
     * @param request {@link UserUpdateRequest}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint to update a user.")
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody UpdateUserRequestDTO request) throws ServiceException {
        LOGGER.info("Initiating request to update user with id: {}.", id);
        final UpdateUserResponseDTO response = userService.updateUser(id, request);

        response.add(linkTo(methodOn(UserController.class).searchUser(id)).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel(DELETE_REL));
        response.add(linkTo(methodOn(UserController.class).listUsers()).withRel(USERS_REL));

        LOGGER.info("User with id {} updated successfully.", id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 
     * @param id      {@link String}
     * @param request {@link ChangeUserStatusRequestDTO}
     * @return {@link ResponseEntity}
     * @throws ServiceException
     */
    @ApiOperation("Endpoint change a user status.")
    @PatchMapping("/{id}/change-status")
    public ResponseEntity<ChangeUserStatusResponseDTO> changeUserStatus(
            @PathVariable String id,
            @RequestBody ChangeUserStatusRequestDTO request) throws ServiceException {
        LOGGER.info("Initiating request to change user: {} status.", id);
        final ChangeUserStatusResponseDTO response = userService.changeUserStatus(id, request);

        response.add(linkTo(methodOn(UserController.class).searchUser(id)).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel(UPDATE_REL));
        response.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel(DELETE_REL));
        response.add(linkTo(methodOn(UserController.class).listUsers()).withRel(USERS_REL));

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
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponseDTO> deleteUser(
            @PathVariable String id) throws ServiceException {
        LOGGER.info("Initiating request to delete user with id: {}.", id);
        final DeleteUserResponseDTO response = userService.deleteUser(id);

        response.add(linkTo(methodOn(UserController.class).searchUser(id)).withRel("user"));

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
    public ResponseEntity<CollectionModel<UserResponseDTO>> listUsers() throws ServiceException {
        List<UserResponseDTO> users = userService.findAllUsers();
        users.forEach(user -> user
                .add(linkTo(methodOn(UserController.class).searchUser(user.getId().toString()))
                        .withSelfRel())
                .add(linkTo(methodOn(UserController.class).updateUser(user.getId().toString(), null))
                        .withRel(UPDATE_REL))
                .add(linkTo(methodOn(UserController.class).deleteUser(user.getId().toString())).withRel(DELETE_REL)));
        CollectionModel<UserResponseDTO> collection = CollectionModel.of(users);
        collection.add(linkTo(methodOn(UserController.class).listUsers()).withSelfRel());
        collection.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
        return ResponseEntity.ok(collection);
    }

}
