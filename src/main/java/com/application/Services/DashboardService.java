package com.application.Services;

import com.application.dto.DashboardStats;
import com.application.Entities.Role;
import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.Repository.TicketRepository;
import com.application.Repository.UserRepository;
import com.application.Security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private TicketRepository ticketRepository;
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    public DashboardStats getDashboardStats() {
        List<Ticket> allTickets = ticketRepository.findAll();
        
        long totalTickets = allTickets.size();
        long openTickets = allTickets.stream()
                .filter(ticket -> "OPEN".equals(ticket.getStatus()))
                .count();
        long inProgressTickets = allTickets.stream()
                .filter(ticket -> "IN_PROGRESS".equals(ticket.getStatus()))
                .count();
        long resolvedTickets = allTickets.stream()
                .filter(ticket -> "RESOLVED".equals(ticket.getStatus()))
                .count();
        long closedTickets = allTickets.stream()
                .filter(ticket -> "CLOSED".equals(ticket.getStatus()))
                .count();

        // Calculate average resolution time
        double averageResolutionTime = allTickets.stream()
                .filter(ticket -> ticket.getResolvedAt() != null && ticket.getCreatedAt() != null)
                .mapToLong(ticket -> {
                    Duration duration = Duration.between(ticket.getCreatedAt(), ticket.getResolvedAt());
                    return duration.toHours();
                })
                .average()
                .orElse(0.0);

        // Count tickets by priority
        Map<String, Long> ticketsByPriority = new HashMap<>();
        ticketsByPriority.put("LOW", allTickets.stream()
                .filter(ticket -> "LOW".equals(ticket.getPriority()))
                .count());
        ticketsByPriority.put("MEDIUM", allTickets.stream()
                .filter(ticket -> "MEDIUM".equals(ticket.getPriority()))
                .count());
        ticketsByPriority.put("HIGH", allTickets.stream()
                .filter(ticket -> "HIGH".equals(ticket.getPriority()))
                .count());
        ticketsByPriority.put("URGENT", allTickets.stream()
                .filter(ticket -> "URGENT".equals(ticket.getPriority()))
                .count());

        return new DashboardStats(
                totalTickets,
                openTickets,
                inProgressTickets,
                resolvedTickets,
                closedTickets,
                averageResolutionTime,
                ticketsByPriority
        );
    }

    // Get stats for a specific user (for role-based access)
    public DashboardStats getDashboardStatsForUser(String userEmail) {

        // Get user from database using email (no need to extract from JWT again)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        Role userRole = user.getRole();

        List<Ticket> userTickets = new ArrayList<>();
        
        if ("ROLE_ADMIN".equals(userRole.getName())) {
            // Admin sees all tickets
            userTickets = ticketRepository.findAll();
        } else if ("ROLE_SUPPORT_AGENT".equals(userRole.getName())) {
            // Support agents see assigned tickets
           // userTickets = ticketRepository.findByAssigneeId(userId);
        } else {
            // Regular users see only their own tickets
            userTickets = ticketRepository.findByOwner(user);
        }
        
        return calculateStatsFromTickets(userTickets);
    }

    private DashboardStats calculateStatsFromTickets(List<Ticket> tickets) {
        long totalTickets = tickets.size();
        long openTickets = tickets.stream()
                .filter(ticket -> "OPEN".equals(ticket.getStatus()))
                .count();
        long inProgressTickets = tickets.stream()
                .filter(ticket -> "IN_PROGRESS".equals(ticket.getStatus()))
                .count();
        long resolvedTickets = tickets.stream()
                .filter(ticket -> "RESOLVED".equals(ticket.getStatus()))
                .count();
        long closedTickets = tickets.stream()
                .filter(ticket -> "CLOSED".equals(ticket.getStatus()))
                .count();

        double averageResolutionTime = tickets.stream()
                .filter(ticket -> ticket.getResolvedAt() != null && ticket.getCreatedAt() != null)
                .mapToLong(ticket -> {
                    Duration duration = Duration.between(ticket.getCreatedAt(), ticket.getResolvedAt());
                    return duration.toHours();
                })
                .average()
                .orElse(0.0);

        Map<String, Long> ticketsByPriority = new HashMap<>();
        ticketsByPriority.put("LOW", tickets.stream()
                .filter(ticket -> "LOW".equals(ticket.getPriority()))
                .count());
        ticketsByPriority.put("MEDIUM", tickets.stream()
                .filter(ticket -> "MEDIUM".equals(ticket.getPriority()))
                .count());
        ticketsByPriority.put("HIGH", tickets.stream()
                .filter(ticket -> "HIGH".equals(ticket.getPriority()))
                .count());
        ticketsByPriority.put("URGENT", tickets.stream()
                .filter(ticket -> "URGENT".equals(ticket.getPriority()))
                .count());

        return new DashboardStats(
                totalTickets,
                openTickets,
                inProgressTickets,
                resolvedTickets,
                closedTickets,
                averageResolutionTime,
                ticketsByPriority
        );
    }
}