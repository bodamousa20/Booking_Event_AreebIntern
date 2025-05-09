package com.events.booking.web.Controller;

import com.events.booking.web.Dto.UserDto;
import com.events.booking.web.Enum.Status;
import com.events.booking.web.Exception.ResourceNotFoundException;
import com.events.booking.web.Model.Image;
import com.events.booking.web.Repository.ImageRepo;
import com.events.booking.web.Requests.ApiResponse;
import com.events.booking.web.Requests.CreateUserRequest;
import com.events.booking.web.Requests.loginRequest;
import com.events.booking.web.Services.AuthService;
import com.events.booking.web.Services.EventServices;
import com.events.booking.web.Services.UserServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserServices userServices;
    private final ImageRepo imageRepository ;
    private EventServices eventServices;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            UserDto newUser = authService.registerUser(request);
            return ResponseEntity.ok(new ApiResponse(Status.SUCCESS, newUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(Status.FAILURE , null));
        }
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody loginRequest request) {
        try {
            return ResponseEntity.ok(new ApiResponse(Status.SUCCESS, authService.login(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(Status.FAILURE, null));
        }
    }

    // Get User Profile by ID
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfile(@RequestHeader("Authorization") String auth) {
        try {
            Long id = eventServices.getIdFromAuthorizationHeader(auth);
            UserDto userProfile = userServices.getUserProfileById(id);
            return ResponseEntity.ok(new ApiResponse(Status.SUCCESS, userProfile));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/events")
    public ResponseEntity<ApiResponse> getAllEvents() {
        try {
            return ResponseEntity.ok(new ApiResponse(Status.SUCCESS, eventServices.getAllEvents()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(Status.FAILURE, null));
        }
    }

    @GetMapping("/event/{event_id}")
    public ResponseEntity<ApiResponse> getEvent(@PathVariable Long event_id) {
        try {
            return ResponseEntity.ok(new ApiResponse(Status.SUCCESS, userServices.getEventById(event_id)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(Status.FAILURE, null));
        }

    }
    @GetMapping("/download-image/{id}")
    public ResponseEntity<byte[]> getCvImage(@PathVariable Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image.getImage());
    }

}
