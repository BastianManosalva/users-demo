package com.example.usersdemo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;
import com.example.usersdemo.utils.JwtUtil;
import com.example.usersdemo.utils.MessageUtils;
import com.example.usersdemo.utils.Utils;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private Utils utils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID testUuid;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        testUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        String email = "test@gmail.cl";
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null));
    }

    @Test
    void findUser_userFound() {

        User user = new User();
        user.setId(testUuid);
        user.setEmail("test@gmail.cl");
        user.setName("Test User");
        user.setIsActive(true);
        user.setCreatedAt(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken("token");
        user.setPhones(new ArrayList<>());

        when(utils.findUser(testUuid.toString())).thenReturn(user);

        UserResponseDTO result = userService.searchUser(testUuid.toString());

        assertNotNull(result);
        assertEquals(testUuid, result.getId());
        assertEquals("test@gmail.cl", result.getEmail());
        assertEquals("Test User", result.getName());
    }

    @Test
    void findUser_otherEmailExcepcion() {
        User user = new User();
        user.setId(testUuid);
        user.setEmail("bad_email@gmail.cl");

        when(utils.findUser(testUuid.toString()))
                .thenThrow(new ServiceException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                        MessageUtils.UNAUTHORIZED_STATUS));

        ServiceException ex = assertThrows(ServiceException.class, () -> userService.searchUser(testUuid.toString()));
        assertEquals(String.valueOf(HttpStatus.UNAUTHORIZED.value()), ex.getCode());
    }

    @Test
    void createUser_userCreated() {

        List<PhoneRequestDTO> phones = new ArrayList<>();
        PhoneRequestDTO phoneRequest = new PhoneRequestDTO();
        phoneRequest.setNumber("12345678");
        phoneRequest.setCityCode("1");
        phoneRequest.setCountryCode("56");
        phones.add(phoneRequest);

        UserRequestDTO request = UserRequestDTO.builder()
                .email("new_email@gmail.cl")
                .password("Pass123")
                .name("new user")
                .phones(phones)
                .build();

        when(userRepo.findByEmail("new_email@gmail.cl")).thenReturn(null);
        when(passwordEncoder.encode("Pass123")).thenReturn("hashedPass");
        when(jwtUtil.generateToken("new_email@gmail.cl")).thenReturn("fake-jwt-token");

        User savedUser = new User();
        savedUser.setId(testUuid);
        savedUser.setEmail("new_email@gmail.cl");
        savedUser.setPassword("hashedPass");
        savedUser.setName("new user");
        savedUser.setIsActive(true);
        savedUser.setCreatedAt(new Date());
        savedUser.setToken("fake-jwt-token");
        savedUser.setPhones(new ArrayList<>());

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        RegisterUserResponseDTO result = userService.createUser(request);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getIsActive());
        assertEquals("fake-jwt-token", result.getToken());

    }

    @Test
    void updateUser_infoUpdated() {

        User user = new User();
        user.setId(testUuid);
        user.setEmail("test@gmail.cl");
        user.setName("old name");
        user.setPassword("oldpass");
        user.setPhones(new ArrayList<>());
        user.setCreatedAt(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken("token");
        user.setIsActive(true);

        UpdateUserRequestDTO request = new UpdateUserRequestDTO();
        request.setEmail("test@gmail.cl");
        request.setName("new name");
        request.setPassword("newpass");
        request.setPhones(new ArrayList<>());

        when(utils.findUser(testUuid.toString())).thenReturn(user);
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("newpass")).thenReturn("hashedNewPass");

        UpdateUserResponseDTO response = userService.updateUser(testUuid.toString(), request);

        assertNotNull(response);
        assertEquals(testUuid, response.getId());
        assertEquals("new name", user.getName());
        assertEquals("test@gmail.cl", user.getEmail());
        assertEquals("hashedNewPass", user.getPassword());
        assertEquals(MessageUtils.UPDATE_USER_MESSAGE, response.getMessage());
        verify(userRepo, times(1)).save(user);

    }

    @Test
    void changeUserStatus_activateUser_OK() {

        User user = new User();
        user.setId(testUuid);
        user.setEmail("test@gmail.cl");
        user.setIsActive(false);

        when(utils.findUser(testUuid.toString())).thenReturn(user);
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChangeUserStatusRequestDTO request = new ChangeUserStatusRequestDTO();
        request.setActive(true);

        ChangeUserStatusResponseDTO response = userService.changeUserStatus(testUuid.toString(), request);

        assertNotNull(response);
        assertEquals(testUuid, response.getId());
        assertTrue(response.isActive());
        verify(userRepo, times(1)).save(user);

    }

    @Test
    void deleteUser_userDeleted() {

        User user = new User();
        user.setId(testUuid);
        user.setName("Test User");
        user.setPhones(new ArrayList<>());
        user.setEmail("test@gmail.cl");

        when(utils.findUser(testUuid.toString())).thenReturn(user);

        DeleteUserResponseDTO response = userService.deleteUser(testUuid.toString());

        verify(userRepo, times(1)).save(user);
        verify(userRepo, times(1)).deleteById(testUuid);

        assertNotNull(response);
        assertEquals(testUuid.toString(), response.getId().toString());
        assertEquals("Test User", response.getName());
        assertNotNull(response.getDeletionDate());
        assertNotNull(response.getMessage());

    }

}
