package com.application.Security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationTime;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
    }

    public String generateToken(String email, String role) {

         Map<String, Object> claims = new HashMap<>();
         claims.put("role", role);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 1h expiry
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }

    public String extractUsername(String token) {

        System.out.println("🔍 JwtUtil - Starting token extraction");
        String email =  Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

         System.out.println("👤 JwtUtil - Extracted email: " + email);       

         return email;       
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token)
                .getBody().getExpiration();
        return expiration.before(new Date());
    }
}
