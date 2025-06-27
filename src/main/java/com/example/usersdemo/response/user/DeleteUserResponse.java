package com.example.usersdemo.response.user;

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
public class DeleteUserResponse extends RepresentationModel<DeleteUserResponse> {

    private UUID id;
    private String name;
    private Date deletionDate;
    private String message;

}
