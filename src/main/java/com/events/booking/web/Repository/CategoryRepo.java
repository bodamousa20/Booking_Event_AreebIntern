package com.events.booking.web.Repository;

import com.events.booking.web.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category ,Long> {

    Category findCategoryBycategoryName(String categoryName);
}
