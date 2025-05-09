package com.events.booking.web.Services;

import com.events.booking.web.Dto.Events_Dto;
import com.events.booking.web.Dto.UserDto;
import com.events.booking.web.Exception.ResourceNotFoundException;
import com.events.booking.web.Mapper.EventMapper;
import com.events.booking.web.Mapper.UserMapper;
import com.events.booking.web.Model.Events;
import com.events.booking.web.Model.UserEntity;
import com.events.booking.web.Repository.EventsRepo;
import com.events.booking.web.Repository.UserRepo;
import com.events.booking.web.Requests.CreateUserRequest;
import com.events.booking.web.Requests.loginRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
public class UserServices {
    private final UserRepo userRepository;
    private final UserMapper userMapper;
    private final EventsRepo eventsRepository ;
    private final EventMapper eventMapper ;


    public UserDto getUserProfileById(Long id) {

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResolutionException("User is Not Found"));

        return userMapper.convertToDtoMethod(user);
    }

    public Events_Dto getEventById(Long id ) {
        Events event =  eventsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("The Event is Not found"));
        return eventMapper.convertToDtoMethod( event ) ;
    }


    }
