package com.application.Services;


import com.application.Entities.Role;
import com.application.Entities.User;
import com.application.Repository.RoleRepository;
import com.application.Repository.UserRepository;
import com.application.Security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public Map<String, String> registerUser(String name, String email, String rawPassword) {
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        userRepository.save(user);

        return Map.of(
                "role", role.getName(),
                "email", user.getEmail()
        );
    }

    @Override
    public Map<String, String> loginUser(String email, String rawPassword) {
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());

        return Map.of(
                "token", token,
                "role", user.getRole().getName(),
                "email", user.getEmail()
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    
}
