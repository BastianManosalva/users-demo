package com.example.usersdemo.response.user;

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
public class ChangeUserStatusResponse extends RepresentationModel<ChangeUserStatusResponse> {

    private UUID id;
    private boolean isActive;

}
