package com.events.booking.web.Services;

import com.events.booking.web.Dto.Events_Dto;
import com.events.booking.web.Exception.ResourceNotFoundException;
import com.events.booking.web.Mapper.EventMapper;
import com.events.booking.web.Model.Category;
import com.events.booking.web.Model.Events;
import com.events.booking.web.Model.Image;
import com.events.booking.web.Repository.*;
import com.events.booking.web.Requests.CreateEventRequest;
import com.events.booking.web.Requests.UpdateEventRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AdminServices {
    private final EventsRepo eventsRepository ;
    private final EventMapper eventMapper;
    private final CategoryRepo categoryRepository ;
    private final ImageRepo imageRepository;

    @Transactional
    public Events_Dto createEvent(CreateEventRequest request , MultipartFile eventImage) throws IOException {
        Events newEvent = new Events();
        newEvent.setCreatedAt(LocalDateTime.now());
        newEvent.setEventName(request.getEventName());
        newEvent.setPrice(request.getPrice());
        newEvent.setAvailableTickets(request.getAvailableTickets());
        if (request.getAvailableTickets() > 0) {
            newEvent.setIsAvailable(Boolean.TRUE);
        } else {
            newEvent.setIsAvailable(Boolean.FALSE);
        }
        newEvent.setDescription(request.getDescription());
        newEvent.setVenue(request.getVenue());
        //check category
        Category category =   categoryRepository.findCategoryBycategoryName(request.getCategory());
        if(category == null){
            //create one
            Category newCategory = new Category();
            newCategory.setCategoryName(request.getCategory());
            categoryRepository.save(newCategory);
            newEvent.setCategory(newCategory);
        }
        else {
            newEvent.setCategory(category);
        }
        Image newImage = new Image();
        newImage.setFileName(eventImage.getOriginalFilename());
        newImage.setFileType(eventImage.getContentType());
        newImage.setImage(eventImage.getBytes());
        imageRepository.save(newImage);
        newImage.setDownloadUrl("http://localhost:5000/api/users/download-image/"+newImage.getId());
        imageRepository.save(newImage);

        newEvent.setEventImage(newImage);

        eventsRepository.save(newEvent);

        return EventMapper.convertToDtoMethod(eventsRepository.save(newEvent));

    }



    public void deleteEventById(Long eventId) {
        Events event =  eventsRepository.findById(eventId).orElseThrow(()->new ResourceNotFoundException("The Event is Not found"));
        eventsRepository.delete(event);
    }
@Transactional
    public Events_Dto UpdateEvent(Long eventId , UpdateEventRequest request){

        try {
            Events event =   eventsRepository.findById(eventId)
                       .orElseThrow(()->new ResourceNotFoundException("the Event is not found "));

            event.setEventName(request.getEventName());
            event.setAvailableTickets(request.getAvailableTickets());
            event.setPrice(request.getPrice());
            event.setVenue(request.getVenue());
            event.setDescription(request.getDescription() );
            event.setIsAvailable(request.getIsAvailable());
           Events newEvent = eventsRepository.save(event);
          return EventMapper.convertToDtoMethod(newEvent);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }





}
