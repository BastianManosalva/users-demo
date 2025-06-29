package com.example.usersdemo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.usersdemo.dto.phone.PhoneResponseDTO;
import com.example.usersdemo.dto.user.UserRequestDTO;
import com.example.usersdemo.dto.user.UserResponseDTO;
import com.example.usersdemo.models.entity.Phone;
import com.example.usersdemo.models.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    /**
     * Converts a User entity to a UserResponse DTO.
     * 
     * @param user {@link User}
     * @return {@link UserResponseDTO}
     */
    public static UserResponseDTO toUserResponse(User user) {
        if (user == null)
            return null;
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setActive(user.getIsActive() != null ? user.getIsActive() : false);
        response.setCreatedAt(user.getCreatedAt());
        response.setModified(user.getModified());
        response.setLastLogin(user.getLastLogin());
        response.setToken(user.getToken());
        response.setPhones(
                user.getPhones() != null
                        ? user.getPhones().stream().map(PhoneMapper::toResponseDTO).collect(Collectors.toList())
                        : null);
        return response;
    }

    /**
     * Converts a UserRequest DTO to a User entity.
     * 
     * @param dto {@link UserRequestDTO}
     * @return {@link User}
     */
    public static User toEntity(UserRequestDTO dto) {
        if (dto == null)
            return null;
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        if (dto.getPhones() != null) {
            List<Phone> phones = dto.getPhones().stream()
                    .map(PhoneMapper::toEntity)
                    .collect(Collectors.toList());
            phones.forEach(phone -> phone.setUser(user));
            user.setPhones(phones);
        }
        return user;
    }

    /**
     * Converts a list of Phone entities to a list of PhoneResponse DTOs.
     * 
     * @param phones {@link List<Phone>}
     * @return {@link List<PhoneResponse>}
     */
    public static List<PhoneResponseDTO> toPhoneResponseList(List<Phone> phones) {
        return phones.stream().map(phone -> {
            PhoneResponseDTO resp = new PhoneResponseDTO();
            resp.setNumber(phone.getNumber());
            resp.setCityCode(phone.getCityCode());
            resp.setCountryCode(phone.getCountryCode());
            return resp;
        }).collect(Collectors.toList());
    }

}
