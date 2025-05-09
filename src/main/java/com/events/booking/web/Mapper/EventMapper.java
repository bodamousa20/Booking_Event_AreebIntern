package com.events.booking.web.Mapper;

import com.events.booking.web.Dto.CategoryDto;
import com.events.booking.web.Dto.Events_Dto;
import com.events.booking.web.Dto.ImageDto;
import com.events.booking.web.Model.Category;
import com.events.booking.web.Model.Events;
import com.events.booking.web.Model.Image;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventMapper {

    private static final ModelMapper modelMapper = new ModelMapper();


    public static Events_Dto convertToDtoMethod(Events event) {
        // Map the Event entity to EventDto using ModelMapper
        Events_Dto eventDto = modelMapper.map(event, Events_Dto.class);

        // Fetch the Image associated with the Event (if any)
        Image img = event.getEventImage();  // Assuming Image is directly related to Event

        // Map the Image to ImageDto using ModelMapper
        if (img != null) {
            ImageDto imageDto = new ImageDto();
            imageDto.setId(img.getId());
            imageDto.setFileName(img.getFileName());
            imageDto.setDownloadUrl(img.getDownloadUrl()); // don't access byte[]
            eventDto.setImageDto(imageDto);
        }

        // Fetch the Category associated with the Event (if any)
        Category category = event.getCategory(); // Assuming Category is directly related to Event
        if (category != null) {
            // Map the Category to CategoryDto using ModelMapper
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

            // Set the CategoryDto into Events_Dto
            eventDto.setCategoryDto(categoryDto);
        }

        // Return the fully mapped Events_Dto
        return eventDto;
    }
}