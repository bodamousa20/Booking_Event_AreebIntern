package com.events.booking.web.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {


    @NotEmpty(message = "firstname should not empty")
    private String first_name ;

    @NotEmpty(message = "lastname should not empty")
    private String last_name ;

    @NotEmpty
    @Email(message = "email is Not valid")
    private String email ;

    @Size(min = 7,message = "the password is too short")
    private String password ;
}
