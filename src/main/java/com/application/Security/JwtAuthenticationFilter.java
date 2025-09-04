package com.application.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @Autowired
    private  JwtUtil jwtUtil;

    // public endpoints that should skip JWT validation
    private final List<String> PUBLIC_ENDPOINTS = List.of(
        "/auth/register","auth/login", "/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        System.out.println("🔍 JWT Filter - Processing request: " + requestPath);

        // Skip JWT validation for public endpoints
        for (String path : PUBLIC_ENDPOINTS) {
            if (requestPath.startsWith(path)) {
                System.out.println("⏭️ JWT Filter - Skipping public endpoint: " + path);
                filterChain.doFilter(request, response);
                return;
            }
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔑 JWT Filter - Authorization header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ JWT Filter - No valid Authorization header, skipping JWT validation");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("🎫 JWT Filter - Token extracted: " + (token.length() > 10 ? token.substring(0, 10) + "..." : token));

        if (token.isBlank()) {
            System.out.println("❌ JWT Filter - Token is blank, skipping JWT validation");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            System.out.println("🚀 JWT Filter - Starting JWT validation");
            String username = jwtUtil.extractUsername(token);
            System.out.println("👤 JWT Filter - Extracted username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("🔐 JWT Filter - Loading user details and validating token");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Use the already extracted username instead of calling validateToken which extracts again
                if (username.equals(userDetails.getUsername()) && !jwtUtil.isTokenExpired(token)) {
                    System.out.println("✅ JWT Filter - Token validated successfully, setting authentication");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("❌ JWT Filter - Token validation failed");
                }
            } else {
                System.out.println("ℹ️ JWT Filter - Username is null or authentication already exists");
            }
        } catch (Exception e) {
            // optional: log the exception
            System.out.println("💥 JWT Filter - Exception during validation: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("➡️ JWT Filter - Continuing filter chain");
        filterChain.doFilter(request, response);
    }
}
