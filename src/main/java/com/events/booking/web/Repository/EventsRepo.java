package com.events.booking.web.Repository;

import com.events.booking.web.Model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepo extends JpaRepository<Events ,Long> {
}
