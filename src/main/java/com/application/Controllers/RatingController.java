package com.application.Controllers;


import com.application.Entities.Rating;
import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.Repository.RatingRepository;
import com.application.Repository.TicketRepository;
import com.application.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingRepository ratingRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    
    // ⭐ Add rating to a ticket
    @PostMapping
    public ResponseEntity<Rating> addRating(@PathVariable Long ticketId,
                                            @RequestBody Rating rating,
                                            Principal principal) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!"CLOSED".equalsIgnoreCase(ticket.getStatus())) {
            return ResponseEntity.badRequest().build(); // Only closed tickets can be rated
        }

       User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

        rating.setTicket(ticket);
        rating.setUser(user);
        Rating saved = ratingRepository.save(rating);

        return ResponseEntity.ok(saved);
    }

    // 📖 View ratings for a ticket (Admin/Agent)
    @GetMapping
    public ResponseEntity<List<Rating>> getRatings(@PathVariable Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return ResponseEntity.ok(ratingRepository.findByTicket(ticket));
    }
}
