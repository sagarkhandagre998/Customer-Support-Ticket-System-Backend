package com.application.Repository;

import com.application.Entities.Ticket;
import com.application.Entities.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByOwner(User owner);
    List<Ticket> findByAssignee(User assignee);

    // Search & filter support
    List<Ticket> findBySubjectContainingIgnoreCase(String subject);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByPriority(String priority);
    List<Ticket> findByAssigneeId(Long userId);
    List<Ticket> findByOwnerId(Long userId);

    
}
