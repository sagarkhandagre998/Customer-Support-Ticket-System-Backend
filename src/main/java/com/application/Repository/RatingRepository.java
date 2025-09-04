package com.application.Repository;


import com.application.Entities.Rating;
import com.application.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByTicket(Ticket ticket);
}

