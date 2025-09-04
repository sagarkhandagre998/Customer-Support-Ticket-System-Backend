package com.application.Controllers;

import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.Services.TicketService;
import com.application.Services.UserService;
import com.application.dto.TicketFilters;

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
    @Autowired
    private final UserService userService;

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


    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "assigneeId", required = false) String assigneeId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            Authentication authentication) {
        
        // Get current user from authentication
        System.out.println("In get all tickets controller");
        String userEmail = authentication.getName();
        User currentUser = userService.getUserByEmail(userEmail);
        
        // Create filter object
        TicketFilters filters = TicketFilters.builder()
                .status(status)
                .priority(priority)
                .search(search)
                .category(category)
                .assigneeId(assigneeId)
                .build();
        
        // Get tickets based on user role and filters
        List<Ticket> tickets = ticketService.getTicketsWithFilters(currentUser, filters, page, size);
        
        return ResponseEntity.ok(tickets);
    }


    
    // // Reassign ticket to another agent (admin only)
    // @PutMapping("/{id}/reassign")
    // public Ticket reassignTicket(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
    //     Ticket ticket = ticketRepository.findById(id).orElseThrow();
    //     User admin = userRepository.findByEmail(auth.getName()).orElseThrow();

    //     if (!admin.getRole().getName().equals("ADMIN")) {
    //         throw new RuntimeException("Only admin can reassign tickets");
    //     }

    //     User newAgent = userRepository.findByEmail(body.get("agentEmail")).orElseThrow();
    //     ticket.setAssignee(newAgent);

    //     return ticketRepository.save(ticket);
    // }
}
