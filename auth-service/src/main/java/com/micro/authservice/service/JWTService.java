package com.micro.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String jwtSecret; // ✅ Inject from config server
    // ✅ Use a static, shared secret key (same as in gateway-service)
    private Key secretKey;

    private final long expirationMillis = 60 * 60 * 1000;

    // ✅ Initialize secret key after the property is loaded
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ✅ Generate token using shared secret key
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}

// ✅ Now both auth-service and gateway-service are aligned and use:
// "my-very-secret-key-1234567890123456" as the HMAC SHA key.

// -------------------------------------
// ✅ Recommendation:
// -------------------------------------
// - In production, use environment variables or Spring Cloud Config to store the key.
// - Never hardcode secret keys in source code.
// - Rotate keys periodically if needed.
