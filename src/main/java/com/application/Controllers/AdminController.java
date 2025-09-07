package com.application.Controllers;
import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.Services.AdminService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
     private final AdminService adminService;

    // ✅ USER MANAGEMENT

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // add user with role 
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {


        System.out.println("In addUser controller " + user);
        return adminService.addUser(user);
    }

    // Assign role to user
    @PutMapping("/users/{id}/role")
    public User assignRole(@PathVariable Long id, @RequestParam String roleName) {
        return adminService.assignRole(id, roleName);
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id);
    }



    
    // ✅ TICKET MANAGEMENT

     //  Get all tickets
    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        return adminService.getAllTickets();
    }

    // Get ticket by ID
    @GetMapping("/tickets/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        return adminService.getTicketById(id);
    }


    // Force update ticket status
    @PatchMapping("/tickets/{id}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long id, @RequestParam String status) {
        Ticket updated = adminService.forceUpdateTicketStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // Force reassign ticket to Agent 
    @PatchMapping("/tickets/{id}/assign")
    public ResponseEntity<Ticket> reassignTicket(@PathVariable Long id, @RequestParam Long userId) {
        Ticket updated = adminService.reassignTicket(id, userId);
        return ResponseEntity.ok(updated);
    }


}
