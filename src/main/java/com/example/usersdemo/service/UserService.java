package com.example.usersdemo.service;

import java.util.List;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;
import com.example.usersdemo.response.user.UserResponse;

public interface UserService {

    /**
     * Searches a user by ID.
     *
     * @param id {@link String}
     * @return {@link UserResponse}
     * @throws ServiceException
     */
    public UserResponse searchUser(String id) throws ServiceException;

    /**
     * Creates a new user.
     *
     * @param request {@link UserRequest}
     * @return {@link RegisterUserResponse}
     * @throws ServiceException
     */
    public RegisterUserResponse createUser(UserRequest request) throws ServiceException;

    /**
     * Updates an existing user.
     *
     * @param id      {@link String}
     * @param request {@link UpdateUserRequest}
     * @return {@link UpdateUserResponse}
     * @throws ServiceException
     */
    public UpdateUserResponse updateUser(String id, UpdateUserRequest request) throws ServiceException;

    /**
     * Changes the status of a user (activate/inactive).
     * 
     * @param id      {@link String}
     * @param request {@link ChangeUserStatusRequest}
     * @return {@link ChangeUserStatusResponse}
     * @throws ServiceException
     */
    public ChangeUserStatusResponse changeUserStatus(String id, ChangeUserStatusRequest request);

    /**
     * Deletes a user by ID.
     * 
     * @param id {@link String}
     * @return {@link DeleteUserResponse}
     * @throws ServiceException
     */
    public DeleteUserResponse deleteUser(String id) throws ServiceException;

    /**
     * List all users.
     *
     * @return {@link List<User>}
     * @throws ServiceException
     */
    List<UserResponse> findAllUsers() throws ServiceException;

}
