package com.example.usersdemo.service;

import java.util.List;

import com.example.usersdemo.dto.user.ChangeUserStatusRequestDTO;
import com.example.usersdemo.dto.user.ChangeUserStatusResponseDTO;
import com.example.usersdemo.dto.user.DeleteUserResponseDTO;
import com.example.usersdemo.dto.user.RegisterUserResponseDTO;
import com.example.usersdemo.dto.user.UpdateUserRequestDTO;
import com.example.usersdemo.dto.user.UpdateUserResponseDTO;
import com.example.usersdemo.dto.user.UserRequestDTO;
import com.example.usersdemo.dto.user.UserResponseDTO;
import com.example.usersdemo.exception.ServiceException;

public interface UserService {

    /**
     * Searches a user by ID.
     *
     * @param id {@link String}
     * @return {@link UserResponseDTO}
     * @throws ServiceException
     */
    UserResponseDTO searchUser(String id) throws ServiceException;

    /**
     * Creates a new user.
     *
     * @param request {@link UserRequest}
     * @return {@link RegisterUserResponseDTO}
     * @throws ServiceException
     */
    RegisterUserResponseDTO createUser(UserRequestDTO request) throws ServiceException;

    /**
     * Updates an existing user.
     *
     * @param id      {@link String}
     * @param request {@link UpdateUserRequestDTO}
     * @return {@link UpdateUserResponseDTO}
     * @throws ServiceException
     */
    UpdateUserResponseDTO updateUser(String id, UpdateUserRequestDTO request) throws ServiceException;

    /**
     * Changes the status of a user (activate/inactive).
     * 
     * @param id      {@link String}
     * @param request {@link ChangeUserStatusRequestDTO}
     * @return {@link ChangeUserStatusResponseDTO}
     * @throws ServiceException
     */
    ChangeUserStatusResponseDTO changeUserStatus(String id, ChangeUserStatusRequestDTO request) throws ServiceException;

    /**
     * Deletes a user by ID.
     * 
     * @param id {@link String}
     * @return {@link DeleteUserResponseDTO}
     * @throws ServiceException
     */
    DeleteUserResponseDTO deleteUser(String id) throws ServiceException;

    /**
     * List all users.
     *
     * @return {@link List<User>}
     * @throws ServiceException
     */
    List<UserResponseDTO> findAllUsers() throws ServiceException;

}
