package com.application.Controllers;
import com.application.Entities.User;
import com.application.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService; // following proper encapsulation

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(
                    userService.registerUser(body.get("name"),body.get("email"), body.get("password"))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        System.out.print("In auth login handler");
        return ResponseEntity.ok(
                userService.loginUser(body.get("email"), body.get("password"))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        // Use Spring Security's Authentication object instead of manual token extraction
        String userEmail = authentication.getName();
        
        // Get user from database using email
        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Return user object
        return ResponseEntity.ok(user);
    }
   

}

