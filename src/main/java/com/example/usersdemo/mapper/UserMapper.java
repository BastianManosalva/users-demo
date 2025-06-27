package com.example.usersdemo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.usersdemo.models.entity.Phone;
import com.example.usersdemo.models.entity.User;
import com.example.usersdemo.response.user.PhoneResponse;
import com.example.usersdemo.response.user.UserResponse;

public class UserMapper {

    /**
     * Converts a User entity to a UserResponse DTO.
     * 
     * @param user {@link User}
     * @return {@link UserResponse}
     */
    public static UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setModified(user.getModified());
        response.setLastLogin(user.getLastLogin());
        response.setToken(user.getToken());
        response.setPhones(toPhoneResponseList(user.getPhones()));
        return response;
    }

    /**
     * Converts a list of Phone entities to a list of PhoneResponse DTOs.
     * 
     * @param phones {@link List<Phone>}
     * @return {@link List<PhoneResponse>}
     */
    public static List<PhoneResponse> toPhoneResponseList(List<Phone> phones) {
        return phones.stream().map(phone -> {
            PhoneResponse resp = new PhoneResponse();
            resp.setNumber(phone.getNumber());
            resp.setCityCode(phone.getCityCode());
            resp.setCountryCode(phone.getCountryCode());
            return resp;
        }).collect(Collectors.toList());
    }

}
