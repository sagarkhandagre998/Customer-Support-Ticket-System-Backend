package com.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketFilters {
    private String status;
    private String priority;
    private String search;
    private String category;
    private String assigneeId;
    private String ownerId;
}
