package com.example.usersdemo.dto.user;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.example.usersdemo.dto.phone.PhoneResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponseDTO extends RepresentationModel<UpdateUserResponseDTO> {

    private String message;
    private UUID id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String token;
    private Boolean isActive;
    private List<PhoneResponseDTO> phones;

}
