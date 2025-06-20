package com.example.usersdemo.response.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserResponse {

    private String id;
    private String name;
    private Date deletionDate;
    private String message;

}
