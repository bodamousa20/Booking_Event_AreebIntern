package com.events.booking.web.Controller;
import org.springframework.security.core.Authentication;

import com.events.booking.web.Dto.Events_Dto;
import com.events.booking.web.Enum.Status;
import com.events.booking.web.Requests.ApiResponse;
import com.events.booking.web.Requests.CreateEventRequest;
import com.events.booking.web.Requests.UpdateEventRequest;
import com.events.booking.web.Services.AdminServices;
import com.events.booking.web.Services.EventServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private EventServices eventServices ;

    private AdminServices adminServices ;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(
            @ModelAttribute CreateEventRequest request,
            @RequestParam("eventImage") MultipartFile eventImage,
            @RequestHeader("Authorization")String auth
    ) {
        try {
            Events_Dto createdEvent = adminServices.createEvent(request, eventImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(Status.FAILURE,null));
        }
    }


    @DeleteMapping("/delete-event/{event_id}")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long event_id) {
        try {
            adminServices.deleteEventById(event_id);
            return ResponseEntity.ok(new ApiResponse(Status.DeletedSuccessfully,null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(Status.FAILURE, null));
        }
    }

    @PutMapping("/update-event/{event_id}")
    public ResponseEntity<ApiResponse>updateEvent(@PathVariable Long event_id , @RequestBody UpdateEventRequest request){
        try {
          Events_Dto event =   adminServices.UpdateEvent(event_id,request);

          return  ResponseEntity.ok(new ApiResponse(Status.UpdatedSuccessfully,event));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(Status.FAILURE,null));
        }

    }

}
