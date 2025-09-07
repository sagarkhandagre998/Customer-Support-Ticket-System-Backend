package com.application.Controllers;

import com.application.Entities.Ticket;
import com.application.Services.TicketService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    @Autowired
    private final TicketService ticketService;

    // Create new ticket
    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(
            @RequestParam(value = "subject", required = true) String subject,
            @RequestParam(value = "description", required = true) String description,
            @RequestParam(value = "priority", required = true) String priority,
            @RequestParam(value = "category", required = false) String category,
            Authentication auth) {
  
        // Create a map to pass to the service
        Map<String, String> body = Map.of(
            "subject", subject,
            "description", description,
            "priority", priority,
            "category",category
        );
        
        Ticket ticketCreated = ticketService.createTicket(body, auth);
        return ResponseEntity.ok(ticketCreated);
    }

    // Get all tickets for logged-in user
    @GetMapping("/my-tickets")
    public List<Ticket> getMyTickets(Authentication authentication) {

        // Use Spring Security's Authentication object instead of manual token extraction
        String userEmail = authentication.getName();
        return ticketService.getMyTickets(userEmail);
       
    }

    // Get ticket by ID
    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id, Authentication auth) {
        return ticketService.getTicketById(id, auth.getName());
    }


    // Update ticket status (only agent or admin)
    @PutMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
     
        return ticketService.updateStatus(id, auth.getName(), body.get("status"));
    
    }


}
