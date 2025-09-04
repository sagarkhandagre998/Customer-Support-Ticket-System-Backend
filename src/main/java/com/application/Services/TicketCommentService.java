package com.application.Services;
import com.application.Entities.TicketComment;

import java.util.List;

public interface TicketCommentService {

    TicketComment addComment(Long ticketId, String email, String text);
    List<TicketComment> getComments(Long ticketId, String userEmail);


}
