package com.application.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stars;   // 1–5
    private String feedback;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User user;   // who gave the rating
}

