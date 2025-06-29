package com.example.usersdemo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usersdemo.dto.phone.PhoneRequestDTO;
import com.example.usersdemo.dto.user.ChangeUserStatusRequestDTO;
import com.example.usersdemo.dto.user.ChangeUserStatusResponseDTO;
import com.example.usersdemo.dto.user.DeleteUserResponseDTO;
import com.example.usersdemo.dto.user.RegisterUserResponseDTO;
import com.example.usersdemo.dto.user.UpdateUserRequestDTO;
import com.example.usersdemo.dto.user.UpdateUserResponseDTO;
import com.example.usersdemo.dto.user.UserRequestDTO;
import com.example.usersdemo.dto.user.UserResponseDTO;
import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.mapper.UserMapper;
import com.example.usersdemo.models.entity.Phone;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
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
    public UserResponseDTO searchUser(String id) {
        LOGGER.info("Searching user with id: {}", id);
        User user = utils.findUser(id);
        LOGGER.info("User found: {}", user != null ? user.getEmail() : "not found");
        return UserMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public RegisterUserResponseDTO createUser(final UserRequestDTO request) {
        LOGGER.info("Creating user with email: {}", request.getEmail());
        utils.emailValidator(request.getEmail());
        utils.passwordValidator(request.getPassword());
        utils.existingEmailValidator(request.getEmail());
        utils.phoneListValidator(request.getPhones());

        Date date = new Date();

        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user.setCreatedAt(date);
        user.setModified(date);
        user.setLastLogin(date);
        user.setToken(jwtUtil.generateToken(user.getEmail()));

        final var createdUser = userRepo.save(user);
        LOGGER.info("User created successfully with id: {}", createdUser.getId());

        return RegisterUserResponseDTO.builder()
                .id(createdUser.getId())
                .isActive(createdUser.getIsActive())
                .created(createdUser.getCreatedAt())
                .modified(createdUser.getModified())
                .lastLogin(createdUser.getLastLogin())
                .token(createdUser.getToken())
                .build();

    }

    @Override
    @Transactional
    public UpdateUserResponseDTO updateUser(final String id, final UpdateUserRequestDTO request) {
        LOGGER.info("Updating user with id: {}", id);
        final var user = utils.findUser(id);
        utils.emailValidator(request.getEmail());
        utils.existingEmailValidator(request.getEmail());
        utils.phoneListValidator(request.getPhones());
        utils.passwordValidator(request.getPassword());
        utils.existingEmailValidator(request.getEmail());

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setModified(new Date());
        updatePhones(user, request.getPhones());

        final var updatedUser = userRepo.save(user);
        LOGGER.info("User updated with id: {}", updatedUser.getId());

        UpdateUserResponseDTO response = new UpdateUserResponseDTO();
        response.setId(updatedUser.getId());
        response.setCreated(updatedUser.getCreatedAt());
        response.setModified(updatedUser.getModified());
        response.setLastLogin(updatedUser.getLastLogin());
        response.setToken(updatedUser.getToken());
        response.setIsActive(updatedUser.getIsActive());
        response.setPhones(UserMapper.toPhoneResponseList(updatedUser.getPhones()));
        response.setMessage(MessageUtils.UPDATE_USER_MESSAGE);
        return response;
    }

    @Override
    @Transactional
    public ChangeUserStatusResponseDTO changeUserStatus(String id, ChangeUserStatusRequestDTO request) {
        LOGGER.info("Changing status for user with id: {} to active: {}", id, request.isActive());
        final var user = utils.findUser(id);

        if (request.isActive() && user.getIsActive()) {
            LOGGER.warn("User {} is already active", id);
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    MessageUtils.USER_ALREADY_ACTIVE);
        }

        if (!request.isActive() && !user.getIsActive()) {
            LOGGER.warn("User {} is already inactive", id);
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    MessageUtils.USER_ALREADY_INACTIVE);
        }

        user.setIsActive(request.isActive());
        user.setModified(new Date());

        final var updatedUser = userRepo.save(user);
        LOGGER.info("User status changed for id: {} to active: {}", id, updatedUser.getIsActive());

        var response = new ChangeUserStatusResponseDTO();
        response.setId(updatedUser.getId());
        response.setActive(updatedUser.getIsActive());
        return response;
    }

    @Override
    @Transactional
    public DeleteUserResponseDTO deleteUser(String id) {
        LOGGER.info("Deleting user with id: {}", id);
        final var user = utils.findUser(id);

        user.getPhones().clear();
        userRepo.save(user);

        userRepo.deleteById(UUID.fromString(id));

        final var deletedUser = new DeleteUserResponseDTO();
        deletedUser.setId(user.getId());
        deletedUser.setName(user.getName());
        deletedUser.setDeletionDate(new Date());
        deletedUser.setMessage(MessageUtils.DELETE_USER_MESSAGE);
        LOGGER.info("User deleted with id: {}", id);
        return deletedUser;
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        LOGGER.info("Listing all users");
        Iterable<User> users = userRepo.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates the phone numbers of a user.
     * 
     * @param user          {@link User}
     * @param phoneRequests {@link List<PhoneRequest>}
     */
    private void updatePhones(User user, List<PhoneRequestDTO> phoneRequests) {
        List<Phone> currentPhones = user.getPhones();
        List<Phone> newPhones = phoneRequests.stream()
                .map(phoneReq -> {
                    Phone phone = new Phone();
                    phone.setNumber(phoneReq.getNumber());
                    phone.setCityCode(phoneReq.getCityCode());
                    phone.setCountryCode(phoneReq.getCountryCode());
                    phone.setUser(user);
                    return phone;
                })
                .collect(Collectors.toList());

        currentPhones.removeIf(
                phone -> newPhones.stream().noneMatch(newPhone -> newPhone.getNumber().equals(phone.getNumber())));

        for (Phone newPhone : newPhones) {
            Phone existingPhone = currentPhones.stream()
                    .filter(p -> p.getNumber().equals(newPhone.getNumber()))
                    .findFirst()
                    .orElse(null);
            if (existingPhone != null) {
                existingPhone.setCityCode(newPhone.getCityCode());
                existingPhone.setCountryCode(newPhone.getCountryCode());
            } else {
                currentPhones.add(newPhone);
            }
        }
    }
}
