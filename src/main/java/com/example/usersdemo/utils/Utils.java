package com.example.usersdemo.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.usersdemo.config.AppConfig;
import com.example.usersdemo.exception.ServiceException;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.models.repository.UserRepository;

@Component
public class Utils {

    @Autowired
    private AppConfig cfg;

    @Autowired
    UserRepository userRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Method to validate an email address.
     * 
     * @param email {@link String}
     */
    public void emailValidator(final String email) {
        Pattern pattern = Pattern.compile(this.cfg.getRegularExpression().get("email"));
        Matcher validEmail = pattern.matcher(email);
        if (validEmail.find() == false) {
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()), MessageUtils.INVALID_EMAIL);
        }
    }

    /**
     * Method to generate a unique ID.
     * 
     * @return {@link String}
     */
    public static String generateID() {
        final String id = UUID.randomUUID().toString();
        return id;
    }

    /**
     * Method to validate password.
     * 
     * @param password {@link String}
     */
    public void passwordValidator(final String password) {
        Pattern pattern = Pattern.compile(this.cfg.getRegularExpression().get("password"));
        Matcher validPassword = pattern.matcher(password);
        if (validPassword.find() == false) {
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()), MessageUtils.INVALID_PASSWORD);
        }
    }

    /**
     * Methdod to validate if a user exists in the database.
     * 
     * @param user {@link User}
     */
    public static void userValidator(final User user) {
        if (null == user) {
            LOGGER.error("User Error {}", MessageUtils.USER_NOT_FOUND);
            throw new ServiceException(String.valueOf(HttpStatus.NOT_FOUND.value()), MessageUtils.USER_NOT_FOUND);
        }
    }

    /**
     * Method to validate if an email already exists in the database.
     * 
     * @param email {@link String}
     */
    public void existingEmailValidator(final String email) {
        if (null != userRepo.findByEmail(email)) {
            LOGGER.error("Registration Error {}", MessageUtils.EXISTING_EMAIL);
            throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()), MessageUtils.EXISTING_EMAIL);
        }
    }

}
