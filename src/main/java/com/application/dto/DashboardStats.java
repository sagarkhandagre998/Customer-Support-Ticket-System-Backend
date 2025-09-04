package com.application.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardStats {
    private long totalTickets;
    private long openTickets;
    private long inProgressTickets;
    private long resolvedTickets;
    private long closedTickets;
    private double averageResolutionTime;
    private Map<String, Long> ticketsByPriority;

    // Constructor
    public DashboardStats() {}

    public DashboardStats(long totalTickets, long openTickets, long inProgressTickets, 
                        long resolvedTickets, long closedTickets, double averageResolutionTime, 
                        Map<String, Long> ticketsByPriority) {
        this.totalTickets = totalTickets;
        this.openTickets = openTickets;
        this.inProgressTickets = inProgressTickets;
        this.resolvedTickets = resolvedTickets;
        this.closedTickets = closedTickets;
        this.averageResolutionTime = averageResolutionTime;
        this.ticketsByPriority = ticketsByPriority;
    }

   
}
