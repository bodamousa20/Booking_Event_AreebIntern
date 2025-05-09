package com.events.booking.web.Controller;

import com.events.booking.web.Dto.Events_Dto;
import com.events.booking.web.Dto.UserDto;
import com.events.booking.web.Enum.Status;
import com.events.booking.web.Model.Events;
import com.events.booking.web.Requests.ApiResponse;
import com.events.booking.web.Requests.CreateEventRequest;
import com.events.booking.web.Services.EventServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/booking")
@PreAuthorize("hasRole('USER')")
public class BookController {

    private EventServices eventServices ;


    @PostMapping("/book-event/{eventID}")
    public ResponseEntity<ApiResponse> userBookEvent(@PathVariable Long eventID , @RequestHeader("Authorization")String auth){
        try {
       String email =  eventServices.getEmailFromAuthorizationHeader(auth);
            Events e = eventServices.bookEvent(email,eventID);
            return ResponseEntity.ok(new ApiResponse(Status.SUCCESS,e));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse(Status.FAILURE,null));
        }
    }


    @GetMapping("/Booked-events")
    public ResponseEntity<ApiResponse>getBookedEvents(@RequestHeader("Authorization") String authorizationHeader){
        try {
            Long  user_id =  eventServices.getIdFromAuthorizationHeader(authorizationHeader);


            List<Events_Dto> eventsDtoList =  eventServices.getUserBookedEvents(user_id);
            return  ResponseEntity.ok(new ApiResponse(Status.SUCCESS,eventsDtoList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(Status.FAILURE,null));
        }

    }


}
