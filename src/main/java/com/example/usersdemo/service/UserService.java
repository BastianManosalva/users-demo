package com.example.usersdemo.service;

import java.util.List;

import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;

public interface UserService {

    /**
     * Finds a user by ID.
     *
     * @param id {@link String}
     * @return {@link User}
     */
    public User findUser(String id);

    /**
     * Creates a new user.
     *
     * @param request {@link UserRequest}
     * @return {@link RegisterUserResponse}
     */
    public RegisterUserResponse createUser(UserRequest request);

    /**
     * Updates an existing user.
     *
     * @param id      {@link String}
     * @param request {@link UpdateUserRequest}
     * @return {@link UpdateUserResponse}
     */
    public UpdateUserResponse updateUser(String id, UpdateUserRequest request);

    /**
     * Changes the status of a user (activate/inactive).
     *
     * @param id      {@link String}
     * @param request {@link ChangeUserStatusRequest}
     * @return {@link ChangeUserStatusResponse}
     */
    public ChangeUserStatusResponse changeUserStatus(String id, ChangeUserStatusRequest request);

    /**
     * Deletes a user by ID.
     * 
     * @param id {@link String}
     * @return {@link DeleteUserResponse}
     */
    public DeleteUserResponse deleteUser(String id);

    /**
     * List all users.
     *
     * @return {@link List<User>}
     */
    public List<User> findAllUsers();

}
