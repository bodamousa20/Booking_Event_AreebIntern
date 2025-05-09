package com.events.booking.web.Requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateEventRequest {
    @NotEmpty
    private String eventName;

    @NotEmpty
    private String description;

    @NotEmpty
    private String venue;

    @NotEmpty
    private Long price;

    @NotEmpty
    private Long availableTickets;

    @NotEmpty
    private String Category;
}
