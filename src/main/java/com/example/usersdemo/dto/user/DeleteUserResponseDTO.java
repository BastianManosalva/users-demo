package com.example.usersdemo.dto.user;

import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserResponseDTO extends RepresentationModel<DeleteUserResponseDTO> {

    private UUID id;
    private String name;
    private Date deletionDate;
    private String message;

}
