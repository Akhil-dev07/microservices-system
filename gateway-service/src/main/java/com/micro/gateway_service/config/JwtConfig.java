package com.micro.gateway_service.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    // ✅ Inject secret key from application.yml (resolved from config server)
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecretKey jwtSecretKey() {
        // ✅ Define and return the secret key used for signing and verifying JWT tokens
        // Note: This secret must exactly match the one used in auth-service
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
