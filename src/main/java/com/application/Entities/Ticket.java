package com.application.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;


@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String description;
    private String status;   // Open, In Progress, Resolved, Closed
    private String priority; // Low, Medium, High, Urgent
    private String category;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    //  // ✅ File attachments
    // @ElementCollection
    // @CollectionTable(name = "ticket_files", joinColumns = @JoinColumn(name = "ticket_id"))
    // @Column(name = "file_path")
    // private List<String> attachments = new ArrayList<>();

     @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

