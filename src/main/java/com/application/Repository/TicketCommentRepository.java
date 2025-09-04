package com.application.Repository;


import com.application.Entities.Ticket;
import com.application.Entities.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicket(Ticket ticket);
}
