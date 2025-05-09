package com.events.booking.web.Services;

import com.events.booking.web.Dto.UserDto;
import com.events.booking.web.Exception.ResourceNotFoundException;
import com.events.booking.web.Mapper.UserMapper;
import com.events.booking.web.Model.UserEntity;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepo userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder ;
    private final JwtEncoder jwtEncoder ;


    public UserDto registerUser (CreateUserRequest request){
        //check email
        try {
            UserEntity user = userRepository.findUserByEmail(request.getEmail());
            if(user != null){
                throw new RuntimeException("The User is Currently Exist");
            }
            UserEntity newUser = new UserEntity();
            newUser.setFirstName(request.getFirst_name());
            newUser.setLastName(request.getLast_name());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole("USER");

            return userMapper.convertToDtoMethod(userRepository.save(newUser));
        }
        catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    public Map<String, Object> login(loginRequest request) {
        //check if the entity is already exist
        try {
            UserEntity user = userRepository.findUserByEmail(request.getEmail());
            if (user == null) {
                throw new ResourceNotFoundException("This Email is not Exist");
            }
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("The password is not correct");

            }
            String Role = user.getRole();
            return createJwtResponse(Role, user.getId(), user.getEmail());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private Map<String, Object> createJwtResponse(String role, Long id,String email) {
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(3, ChronoUnit.HOURS))
                    .claim("EMAIL",email)
                    .claim("roles", role.toUpperCase()) // Ensure proper role prefix
                    .claim("ID", id)
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", id);
            response.put("roles", List.of(role.toUpperCase()));
            return response;
        }



    }


