package com.example.usersdemo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.mapper.UserMapper;
import com.example.usersdemo.models.entity.Phone;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
import com.example.usersdemo.request.user.ChangeUserStatusRequest;
import com.example.usersdemo.request.user.PhoneRequest;
import com.example.usersdemo.request.user.UpdateUserRequest;
import com.example.usersdemo.request.user.UserRequest;
import com.example.usersdemo.response.user.ChangeUserStatusResponse;
import com.example.usersdemo.response.user.DeleteUserResponse;
import com.example.usersdemo.response.user.RegisterUserResponse;
import com.example.usersdemo.response.user.UpdateUserResponse;
import com.example.usersdemo.response.user.UserResponse;
import com.example.usersdemo.service.UserService;
import com.example.usersdemo.utils.JwtUtil;
import com.example.usersdemo.utils.MessageUtils;
import com.example.usersdemo.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Utils utils;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse searchUser(String id) {
        User user = utils.findUser(id);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public RegisterUserResponse createUser(final UserRequest request) {

        utils.emailValidator(request.getEmail());
        utils.passwordValidator(request.getPassword());
        utils.existingEmailValidator(request.getEmail());
        utils.phoneListValidator(request.getPhones());

        Date date = new Date();

        final var user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user.setCreatedAt(date);
        user.setModified(date);
        user.setLastLogin(date);

        List<Phone> phones = request.getPhones().stream()
                .map(phoneReq -> {
                    Phone phone = new Phone();
                    phone.setNumber(phoneReq.getNumber());
                    phone.setCityCode(phoneReq.getCityCode());
                    phone.setCountryCode(phoneReq.getCountryCode());
                    phone.setUser(user);
                    return phone;
                })
                .collect(Collectors.toList());

        user.setPhones(phones);
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
    @Transactional
    public UpdateUserResponse updateUser(final String id, final UpdateUserRequest request) {
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

        UpdateUserResponse response = new UpdateUserResponse();
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
    public ChangeUserStatusResponse changeUserStatus(String id, ChangeUserStatusRequest request) {

        final var user = utils.findUser(id);

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
    @Transactional
    public DeleteUserResponse deleteUser(String id) {

        final var user = utils.findUser(id);

        user.getPhones().clear();
        userRepo.save(user);

        userRepo.deleteById(UUID.fromString(id));

        final var deletedUser = new DeleteUserResponse();
        deletedUser.setId(user.getId());
        deletedUser.setName(user.getName());
        deletedUser.setDeletionDate(new Date());
        deletedUser.setMessage(MessageUtils.DELETE_USER_MESSAGE);

        return deletedUser;

    }

    @Override
    public List<UserResponse> findAllUsers() {
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
    private void updatePhones(User user, List<PhoneRequest> phoneRequests) {
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
