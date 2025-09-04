package com.application.Controllers;

import com.application.Entities.TicketComment;
import com.application.Services.TicketCommentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final TicketCommentService commentService;

    // Add comment to ticket
    @PostMapping("/{ticketId}")
    public ResponseEntity<TicketComment> addComment(
            @PathVariable Long ticketId,
            @RequestBody String comment,
            Authentication auth) {

        System.out.println("In addComment controller " + comment);

        TicketComment saved = commentService.addComment(ticketId, comment, auth.getName());
        return ResponseEntity.ok(saved);
    }


    // Get all comments for a ticket
    @GetMapping("/{ticketId}")
    public ResponseEntity<List<TicketComment>> getComments(
            @PathVariable Long ticketId,
            Authentication auth) {

        List<TicketComment> comments = commentService.getComments(ticketId, auth.getName());
        return ResponseEntity.ok(comments);
    }

}
