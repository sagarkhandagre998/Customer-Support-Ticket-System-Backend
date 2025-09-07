package com.application.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.Entities.Ticket;
import com.application.Entities.TicketComment;
import com.application.Entities.User;
import com.application.Repository.TicketCommentRepository;
import com.application.Repository.TicketRepository;
import com.application.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    @Autowired
    private final TicketCommentRepository ticketCommentRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Ticket> getAssignedTickets(String userEmail) {
        // Find the user by email
        User agent = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        
        // Check if the user is an agent
        if (!agent.getRole().getName().equals("ROLE_AGENT")) {
            throw new RuntimeException("User is not an agent");
        }
        
        // Get all tickets assigned to this agent
        return ticketRepository.findByAssignee(agent);
    }

    @Override
    public List<Ticket> getAssignedTicketsByStatus(Long agentId, String status) {
        // Find the agent by ID
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + agentId));
        
        // Check if the user is an agent
        if (!agent.getRole().getName().equals("ROLE_AGENT")) {
            throw new RuntimeException("User is not an agent");
        }
        
        // Get all tickets assigned to this agent and filter by status
        return ticketRepository.findByAssignee(agent).stream()
                .filter(ticket -> ticket.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    @Override
    public Ticket updateTicketStatus(Long ticketId, String agentEmail, String status) {
        // Find the ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));
        
        // Find the agent
        User agent = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + agentEmail));
        
        // Check if the user is an agent
        if (!agent.getRole().getName().equals("ROLE_AGENT")) {
            throw new RuntimeException("User is not an agent");
        }
        
        // Verify agent is assigned to this ticket
        if (ticket.getAssignee() == null || !ticket.getAssignee().getEmail().equals(agentEmail)) {
            throw new RuntimeException("Agent is not assigned to this ticket");
        }
        
        // Update ticket status
        ticket.setStatus(status.toUpperCase());
        ticket.setUpdatedAt(java.time.LocalDateTime.now());
        
        // Set resolved timestamp if status is resolved or closed
        if (status.equalsIgnoreCase("RESOLVED") || status.equalsIgnoreCase("CLOSED")) {
            ticket.setResolvedAt(java.time.LocalDateTime.now());
        }
        
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket addCommentToTicket(Long ticketId, String agentemail, String comment) {
        // Find the ticket
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));
        
        // Find the agent
        User agent = userRepository.findByEmail(agentemail)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + agentemail));
        
        // Check if the user is an agent
        if (!agent.getRole().getName().equals("ROLE_AGENT")) {
            throw new RuntimeException("User is not an agent");
        }
        
        // Verify agent is assigned to this ticket
        if (ticket.getAssignee() == null || !ticket.getAssignee().getEmail().equals(agentemail)) {
            throw new RuntimeException("Agent is not assigned to this ticket");
        }
        
        // Create new comment
        TicketComment ticketComment = TicketComment.builder()
                .content(comment)
                .ticket(ticket)
                .user(agent)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        
        // Save comment (you'll need to inject TicketCommentRepository if you want to save comments)
        ticketCommentRepository.save(ticketComment);

        // For now, just update the ticket's timestamp
        ticket.setUpdatedAt(java.time.LocalDateTime.now());
        
        return ticketRepository.save(ticket);
    }
    
}
