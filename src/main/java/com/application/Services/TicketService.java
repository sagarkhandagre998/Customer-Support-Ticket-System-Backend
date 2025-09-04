package com.application.Services;

import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.dto.TicketFilters;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;


public interface TicketService {

    Ticket createTicket(Map<String, String> body, Authentication auth);
    Ticket getTicketById(Long id, String email);
    List<Ticket> getTicketsByOwner(User owner);
    List<Ticket> getTicketsByAssignee(User assignee);
    List<Ticket> getMyTickets(String token);
    Ticket updateStatus(Long id, String email, String status);
    void deleteTicket(Long id); 
    public List<Ticket> getTicketsWithFilters(User currentUser, TicketFilters filters, int page, int size);

}

