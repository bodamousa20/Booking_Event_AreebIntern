package com.events.booking.web.Dto;

import com.events.booking.web.Model.Category;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
public class Events_Dto {
    private Long id;
    private String eventName;
    private String description;
    private LocalDateTime createdAt;
    private String venue;
    private Long price;
    private Long availableTickets;
    private Boolean isAvailable;
    private ImageDto imageDto;
    private CategoryDto categoryDto;
}


