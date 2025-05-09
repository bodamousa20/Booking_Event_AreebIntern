package com.events.booking.web.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class loginRequest {

    @NotEmpty
    @Email(message = "email is not valid")
    private String email ;

    @NotEmpty
    @Size(min = 7)
    private String password ;
}
