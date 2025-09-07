package com.application.Services;

import com.application.Entities.Ticket;
import java.util.List;


public interface AgentService {

    // Ticket Assignment & Management
    List<Ticket> getAssignedTickets(String userEmail);
    List<Ticket> getAssignedTicketsByStatus(Long agentId, String status);
    Ticket updateTicketStatus(Long ticketId, String agentEmail, String status);
    Ticket addCommentToTicket(Long ticketId, String agentEmail, String comment);
    
    
}
