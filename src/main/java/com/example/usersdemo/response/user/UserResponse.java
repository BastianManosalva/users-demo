package com.example.usersdemo.response.user;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends RepresentationModel<UserResponse> {

    private UUID id;
    private String name;
    private String email;
    private List<PhoneResponse> phones;
    private boolean isActive;
    private Date createdAt;
    private Date modified;
    private Date lastLogin;
    private String token;

}
