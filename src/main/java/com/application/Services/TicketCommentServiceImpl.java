package com.application.Services;
import com.application.Entities.Ticket;
import com.application.Entities.TicketComment;
import com.application.Entities.User;
import com.application.Repository.TicketCommentRepository;
import com.application.Repository.TicketRepository;
import com.application.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketCommentServiceImpl implements TicketCommentService {

    private final TicketCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketComment addComment(Long ticketId, String content, String userEmail) {

        System.out.println("In addComment service " + content);

         User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // ✅ Authorization check
        boolean isOwner = ticket.getOwner().getEmail().equals(userEmail);
        boolean isAssignee = ticket.getAssignee() != null &&
                             ticket.getAssignee().getEmail().equals(userEmail);
        boolean isAdmin = user.getRole().getName().equals("ROLE_ADMIN");

        if (!isOwner && !isAssignee && !isAdmin) {
            throw new RuntimeException("Unauthorized to comment on this ticket");
        }

        TicketComment comment = new TicketComment();
        comment.setContent(content);
        comment.setTicket(ticket);
        comment.setUser(user);

        return commentRepository.save(comment);

    }

     @Override
    public List<TicketComment> getComments(Long ticketId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // ✅ Authorization check
        boolean isOwner = ticket.getOwner().getEmail().equals(userEmail);
        boolean isAssignee = ticket.getAssignee() != null &&
                             ticket.getAssignee().getEmail().equals(userEmail);
        boolean isAdmin = user.getRole().getName().equals("ROLE_ADMIN");

        if (!isOwner && !isAssignee && !isAdmin) {
            throw new RuntimeException("Unauthorized to view comments");
        }

        return commentRepository.findByTicket(ticket);
    }
}

