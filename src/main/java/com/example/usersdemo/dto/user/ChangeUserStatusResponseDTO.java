package com.example.usersdemo.dto.user;

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
public class ChangeUserStatusResponseDTO extends RepresentationModel<ChangeUserStatusResponseDTO> {

    private UUID id;
    private boolean isActive;

}
