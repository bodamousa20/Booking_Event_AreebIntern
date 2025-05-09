package com.events.booking.web.Requests;

import lombok.Data;

@Data
public class UpdateEventRequest {

    private String eventName ;
    private String description ;
    private String venue ;
    private Long price ;
    private Long availableTickets ;
    private Boolean isAvailable ;

}
