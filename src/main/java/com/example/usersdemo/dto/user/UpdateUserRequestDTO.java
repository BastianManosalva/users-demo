package com.example.usersdemo.dto.user;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.example.usersdemo.dto.phone.PhoneRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO {

    @NotBlank(message = "The name is mandatory.")
    private String name;

    @NotBlank(message = "The email is mandatory.")
    private String email;

    @NotBlank(message = "The password is mandatory.")
    private String password;

    private List<@Valid PhoneRequestDTO> phones;

}
