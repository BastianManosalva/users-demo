package com.example.usersdemo.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;

public class UtilsTest {

    private Utils utils;
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        utils = new Utils();
        userRepo = mock(UserRepository.class);
        utils.userRepo = userRepo;
    }

    @Test
    void generateID_retornaUUIDValido() {
        String id = Utils.generateID();
        assertNotNull(id);
        assertTrue(id.matches("^[a-f0-9\\-]{36}$"));
    }

    @Test
    void userValidator_usuarioNull_lanzaExcepcion() {
        ServiceException ex = assertThrows(ServiceException.class, () -> Utils.userValidator(null));
        assertEquals(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getCode());
        assertEquals(MessageUtils.USER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void userValidator_validUser() {
        User user = new User();
        assertDoesNotThrow(() -> Utils.userValidator(user));
    }

    @Test
    void existingEmailValidator_validEmail() {
        when(userRepo.findByEmail("new_email@gmail.cl")).thenReturn(null);
        assertDoesNotThrow(() -> utils.existingEmailValidator("new_enmail@gmail.cl"));
    }

    @Test
    void existingEmailValidator_Excepcion() {
        when(userRepo.findByEmail("email@gmail.cl")).thenReturn(new User());
        ServiceException ex = assertThrows(ServiceException.class,
                () -> utils.existingEmailValidator("email@gmail.cl"));
        assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getCode());
        assertEquals(MessageUtils.EXISTING_EMAIL, ex.getMessage());
    }

}
