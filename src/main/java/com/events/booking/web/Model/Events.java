package com.events.booking.web.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String eventName ;
    private String description ;

    private LocalDateTime createdAt ;
    private String venue ;
    private Long price ;

    private Long availableTickets ;

    private Boolean isAvailable = Boolean.FALSE;
    // image class
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_image", referencedColumnName = "id")
    private Image eventImage ;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;




}
