package com.application.Controllers;

import com.application.dto.DashboardStats;
import com.application.Services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        try {
            DashboardStats stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Optional: Get stats for current user
    @GetMapping("/stats/my")
    public ResponseEntity<DashboardStats> getMyDashboardStats(Authentication authentication) {
        try {
            // Use Spring Security's Authentication object instead of manual token extraction
            String userEmail = authentication.getName();
            DashboardStats stats = dashboardService.getDashboardStatsForUser(userEmail);
            System.out.println(stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    
}
