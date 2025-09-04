package com.application.Services;

import java.util.List;

import com.application.Entities.Ticket;
import com.application.Entities.User;

public interface AdminService {

     // User Management by Admin

    List<User> getAllUsers();
    User addUser(User user);
    User assignRole(Long userId, String roleName);
    String deleteUser(Long userId);   

    // Ticket management by Admin

    List<Ticket> getAllTickets();
    Ticket forceUpdateTicketStatus(Long ticketId, String status);
    Ticket reassignTicket(Long ticketId, Long userId);

}
