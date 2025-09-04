package com.application.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.Entities.Role;
import com.application.Entities.Ticket;
import com.application.Entities.User;
import com.application.Repository.RoleRepository;
import com.application.Repository.TicketRepository;
import com.application.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TicketRepository ticketRepository;

    @Autowired
    private final EmailService emailService;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        // ✅ Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ✅ Set default role if none provided
        if (user.getRole() == null) {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            user.setRole(defaultRole);
        }else{

            Role agentRole = roleRepository.findByName("ROLE_AGENT")
                    .orElseThrow(() -> new RuntimeException("AGENT role USER not found"));
            user.setRole(agentRole);
        } 

        return userRepository.save(user);
    }

     @Override
    public User assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        return userRepository.save(user);
    }

     @Override
    public String deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
        return "User deleted successfully";
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket forceUpdateTicketStatus(Long ticketId, String status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(status.toUpperCase());
        Ticket saved = ticketRepository.save(ticket);

        // ✅ Notify owner
        emailService.sendMail(
                ticket.getOwner().getEmail(),
                "Ticket Status Updated",
                "Your ticket #" + ticket.getId() + " is now " + ticket.getStatus()
        );

        return saved;
    }
    
     @Override
    public Ticket reassignTicket(Long ticketId, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User newAssignee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // ✅ Check that user is an agent
        if (!newAssignee.getRole().getName().equals("ROLE_AGENT")) {
            throw new RuntimeException("Only AGENT users can be assigned tickets");
        }

        ticket.setAssignee(newAssignee);
        Ticket saved = ticketRepository.save(ticket);

        // ✅ Notify agent
        emailService.sendMail(
                newAssignee.getEmail(),
                "New Ticket Assigned",
                "You have been assigned ticket #" + ticket.getId() + " with subject: " + ticket.getSubject()
        );

        // ✅ Notify owner
        emailService.sendMail(
                ticket.getOwner().getEmail(),
                "Ticket Reassigned",
                "Your ticket #" + ticket.getId() + " has been reassigned to agent: " + newAssignee.getEmail()
        );

        return saved;
    }

    
}
