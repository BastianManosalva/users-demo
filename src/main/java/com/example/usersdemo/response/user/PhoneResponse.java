package com.example.usersdemo.response.user;

import lombok.Data;

@Data
public class PhoneResponse {

    private String number;
    private String cityCode;
    private String countryCode;

}
