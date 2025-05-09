package com.events.booking.web.Repository;

import com.events.booking.web.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
    UserEntity findUserByEmail(String email);
}
