package com.example.usersdemo.dto.phone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class PhoneRequestDTO {

    @NotBlank(message = "Phone number is mandatory.")
    @Pattern(regexp = "^\\d{9}$", message = "Phone number must be 9 digits.")
    private String number;

    @NotBlank(message = "City code is mandatory.")
    @Pattern(regexp = "^\\d{1,5}$", message = "City code must be 1 to 5 digits.")
    private String cityCode;

    @NotBlank(message = "Country code is mandatory.")
    @Pattern(regexp = "^\\d{1,5}$", message = "Country code must be 1 to 5 digits.")
    private String countryCode;

}
