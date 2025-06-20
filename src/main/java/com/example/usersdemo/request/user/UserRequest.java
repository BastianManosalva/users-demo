package com.example.usersdemo.request.user;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.example.usersdemo.models.entity.Phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @javax.validation.constraints.Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "The name must contain only letters and spaces.")
    @NotBlank(message = "The name is mandatory.")
    private String name;

    @NotBlank(message = "The email is mandatory.")
    private String email;

    @NotBlank(message = "The password is mandatory.")
    private String password;

    private List<Phone> phones;

}
