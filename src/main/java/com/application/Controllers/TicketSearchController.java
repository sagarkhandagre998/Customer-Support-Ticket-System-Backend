package com.application.Controllers;

import com.application.Entities.Ticket;
import com.application.Repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tickets/search")
@RequiredArgsConstructor
public class TicketSearchController {

    private final TicketRepository ticketRepository;

    // 🔎 Search tickets by subject (case-insensitive)
    @GetMapping("/subject")
    public List<Ticket> searchBySubject(@RequestParam String keyword) {
        return ticketRepository.findBySubjectContainingIgnoreCase(keyword);
    }

    // 🔎 Filter tickets by status
    @GetMapping("/status")
    public List<Ticket> filterByStatus(@RequestParam String status) {
        return ticketRepository.findByStatus(status.toUpperCase());
    }

    // 🔎 Filter tickets by priority
    @GetMapping("/priority")
    public List<Ticket> filterByPriority(@RequestParam String priority) {
        return ticketRepository.findByPriority(priority.toUpperCase());
    }

    // 🔎 Filter tickets by assigned agent
    @GetMapping("/assignee/{userId}")
    public List<Ticket> filterByAssignee(@PathVariable Long userId) {
        return ticketRepository.findByAssigneeId(userId);
    }

    // 🔎 Filter tickets by owner
    @GetMapping("/owner/{userId}")
    public List<Ticket> filterByOwner(@PathVariable Long userId) {
        return ticketRepository.findByOwnerId(userId);
    }
}

