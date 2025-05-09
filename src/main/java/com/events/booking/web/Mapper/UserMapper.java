package com.events.booking.web.Mapper;

import com.events.booking.web.Dto.UserDto;
import com.events.booking.web.Model.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserDto convertToDtoMethod(UserEntity user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
}
