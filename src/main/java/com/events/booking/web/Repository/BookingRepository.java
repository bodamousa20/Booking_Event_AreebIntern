package com.events.booking.web.Repository;

import com.events.booking.web.Model.Booking;
import com.events.booking.web.Model.Events;
import com.events.booking.web.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {


    Boolean existsByUserAndEvent(UserEntity user, Events event);

    List<Booking> findByUser(UserEntity user);
}

