package com.application.Controllers;

import com.application.Entities.Ticket;
import com.application.Services.AgentService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_AGENT') ")
public class AgentController {

    @Autowired
    private final AgentService agentService;

    /**
     * Get all tickets assigned to the current agent
     */
    @GetMapping("/tickets/my-assigned-tickets")
    public ResponseEntity<List<Ticket>> getAssignedTickets(Authentication authentication) {

        System.out.println("In getAssignedTickets controller");
         // Use Spring Security's Authentication object instead of manual token extraction
         String agentEmail = authentication.getName();
        List<Ticket> assignedTickets = agentService.getAssignedTickets(agentEmail);
        return ResponseEntity.ok(assignedTickets);
    }

    // Update ticket status (Agent can only update their assigned tickets)
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam String status,
            Authentication authentication) {
        String agentEmail = authentication.getName();
        Ticket ticket = agentService.updateTicketStatus(ticketId, agentEmail, status);
        return ResponseEntity.ok(ticket);
    }

    // Add comment to ticket (Agent can only comment on their assigned tickets)
    @PostMapping("/tickets/{ticketId}/comment")
    public ResponseEntity<Ticket> addCommentToTicket(
            @PathVariable Long ticketId,
            @RequestParam String comment,
            Authentication authentication) {
        String agentEmail = authentication.getName();
        Ticket ticket = agentService.addCommentToTicket(ticketId, agentEmail, comment);
        return ResponseEntity.ok(ticket);
    }

  
}
