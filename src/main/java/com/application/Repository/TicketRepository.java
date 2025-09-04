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

    // Find tickets by owner with filters - simplified to avoid description search
    @Query("SELECT t FROM Ticket t WHERE t.owner.id = :ownerId " +
           "AND (:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority) " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:search IS NULL OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Ticket> findByOwnerIdAndFilters(
            @Param("ownerId") Long ownerId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("category") String category,
            @Param("search") String search,
            Pageable pageable);
    
    // Find tickets by assignee or status - simplified to avoid description search
    @Query("SELECT t FROM Ticket t WHERE (t.assignee.id = :assigneeId OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority) " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:search IS NULL OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Ticket> findByAssigneeOrStatus(
            @Param("assigneeId") Long assigneeId,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("category") String category,
            @Param("search") String search,
            Pageable pageable);
    
    // Admin: Find all tickets with filters - simplified to avoid description search
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:status IS NULL OR t.status = :status) " +
           "AND (:priority IS NULL OR t.priority = :priority) " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId) " +
           "AND (:search IS NULL OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Ticket> findAllWithFilters(
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("category") String category,
            @Param("assigneeId") Long assigneeId,
            @Param("search") String search,
            Pageable pageable);
}
