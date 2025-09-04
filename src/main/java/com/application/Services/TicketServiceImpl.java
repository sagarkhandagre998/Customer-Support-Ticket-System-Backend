package com.application.Services;


import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.Repository.TicketRepository;
import com.application.Repository.UserRepository;
import com.application.Security.JwtUtil;
import com.application.dto.TicketFilters;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    private final JwtUtil jwtUtil;

    
    @Override
    @Transactional
    public Ticket createTicket(Map<String, String> body, Authentication auth) {

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = new Ticket();
        ticket.setSubject(body.get("subject"));
        ticket.setDescription(body.get("description"));
        ticket.setPriority(body.get("priority"));
        ticket.setCategory(body.get("category"));
        ticket.setStatus("OPEN");
        ticket.setOwner(user);

        Ticket saved = ticketRepository.save(ticket);

        // ✅ Notify owner
        emailService.sendMail(
                saved.getOwner().getEmail(),
                "Ticket Created: " + saved.getSubject(),
                "Your ticket #" + saved.getId() +
                        " has been created with status " + saved.getStatus()
        );

        // ✅ Notify admin (optional)
        emailService.sendMail(
                "admin@company.com",
                "New Ticket Raised",
                "A new ticket has been created by " + saved.getOwner().getEmail()
        );

        return saved;
    }
    @Override
    public List<Ticket> getTicketsWithFilters(User currentUser, TicketFilters filters, int page, int size) {
        System.out.println("In get tickets with filters service");
        List<Ticket> tickets;
        
        // Create Pageable object
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        
        // Apply role-based access control
        if ("ROLE_ADMIN".equals(currentUser.getRole().getName())) {
            // Admin sees all tickets
            tickets = ticketRepository.findAllWithFilters(
                filters.getStatus(), 
                filters.getPriority(), 
                filters.getCategory(), 
                filters.getAssigneeId() != null ? Long.valueOf(filters.getAssigneeId()) : null, 
                filters.getSearch(), 
                pageable
            );
        } else if ("ROLE_SUPPORT_AGENT".equals(currentUser.getRole().getName())) {
            // Support agents see assigned tickets and open tickets
            tickets = ticketRepository.findByAssigneeOrStatus(
                currentUser.getId(), 
                "OPEN", 
                filters.getPriority(), 
                filters.getCategory(), 
                filters.getSearch(), 
                pageable
            );
        } else {
            // Regular users see only their own tickets
            tickets = ticketRepository.findByOwnerIdAndFilters(
                currentUser.getId(), 
                filters.getStatus(), 
                filters.getPriority(), 
                filters.getCategory(), 
                filters.getSearch(), 
                pageable
            );
        }
        
        return tickets;
    }

    @Override
    public Ticket getTicketById(Long id, String email) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));

        // Check owner
        if (ticket.getOwner().getEmail().equals(email)) {
            return ticket;
        }

        // Check assignee
        if (ticket.getAssignee() != null && ticket.getAssignee().getEmail().equals(email)) {
            return ticket;
        }

        // Otherwise unauthorized
        throw new RuntimeException("Unauthorized access to ticket " + id);

    }


    @Override
    public List<Ticket> getTicketsByOwner(User owner) {
        return ticketRepository.findByOwner(owner);
    }

    @Override
    public List<Ticket> getTicketsByAssignee(User assignee) {
        return ticketRepository.findByAssignee(assignee);
    }

   @Override
    public List<Ticket> getMyTickets(String userEmail) {

        // Get user from database using email (no need to extract from JWT again)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
        return ticketRepository.findByOwner(user);
    
    }


    @Override
    public Ticket updateStatus(Long id, String email, String status) {
        
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Only admins or agents can update
        String role = user.getRole().getName();
        if (!role.equals("ADMIN") && !role.equals("AGENT")) {
            throw new RuntimeException("Only agents or admins can update status");
        }

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
