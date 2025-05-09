package com.events.booking.web.Services;

import com.events.booking.web.Dto.CategoryDto;
import com.events.booking.web.Dto.Events_Dto;
import com.events.booking.web.Dto.ImageDto;
import com.events.booking.web.Exception.ResourceNotFoundException;
import com.events.booking.web.Mapper.EventMapper;
import com.events.booking.web.Model.*;
import com.events.booking.web.Repository.*;
import com.events.booking.web.Requests.CreateEventRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EventServices {
    private final EventsRepo eventsRepository ;
    private final UserRepo userRepository;
    private final BookingRepository bookingRepository;
    private final JwtDecoder jwtDecoder;
    private final EventMapper eventMapper;
    private final CategoryRepo categoryRepository ;
    private final ImageRepo imageRepository;




@Transactional
    public Events bookEvent(String email , Long eventID){

    UserEntity user = userRepository.findUserByEmail(email);
    if(user== null){
        throw new RuntimeException("user not found");
    }

    Events event = eventsRepository.findById(eventID)
            .orElseThrow(()->new RuntimeException("Event not found"));


    Boolean alreadyBooked = bookingRepository.existsByUserAndEvent(user,event);

    if(alreadyBooked){
        throw new IllegalStateException("You already Booked this event");
    }

    if(Boolean.FALSE.equals(event.getIsAvailable())||event.getAvailableTickets()<=0){
        throw new IllegalStateException("Event is fully booked or unavailable.");
    }

        Booking booking = Booking.builder()
                .user(user)
                .event(event)
                .bookedAt(LocalDateTime.now())
                .build();

    bookingRepository.save(booking);

    event.setAvailableTickets(event.getAvailableTickets() - 1);
    if (event.getAvailableTickets() == 0) {
        event.setIsAvailable(false);
    }
     return eventsRepository.save(event);


}

@Transactional
    public List<Events_Dto> getUserBookedEvents(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings = bookingRepository.findByUser(user);

        return bookings.stream()
                .map(Booking::getEvent)
                .map(EventMapper::convertToDtoMethod)
                .toList();
    }

    public List<Events_Dto> getAllEvents() {


        List<Events> eventsDtoList =  eventsRepository.findAll();

        return eventsDtoList.stream().map(EventMapper::convertToDtoMethod).toList();
    }



    public String getEmailFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        Jwt decodedJwt = jwtDecoder.decode(token);
        return decodedJwt.getClaim("EMAIL");
    }

    public String getRoleFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        Jwt decodedJwt = jwtDecoder.decode(token);
        return decodedJwt.getClaim("ROLE");
    }

    public Long getIdFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid authorization header");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        Jwt decodedJwt = jwtDecoder.decode(token);
        return decodedJwt.getClaim("ID");
    }




}
