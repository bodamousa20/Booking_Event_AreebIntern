package com.events.booking.web.Model;

import com.events.booking.web.Requests.CreateUserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String firstName ;
    private String lastName ;
    private String email ;
    private String password ; // to be hashed
    private String role ;



}
