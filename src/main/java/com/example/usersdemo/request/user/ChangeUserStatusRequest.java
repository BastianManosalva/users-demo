package com.example.usersdemo.request.user;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserStatusRequest {

    @NotBlank(message = "The user status is mandatory.")
    private boolean isActive;

}
