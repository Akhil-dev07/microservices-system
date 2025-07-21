package com.micro.gateway_service.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey() {
        // ✅ Define and return the secret key used for signing and verifying JWT tokens
        // Note: This secret must exactly match the one used in auth-service
        return Keys.hmacShaKeyFor("my-very-secret-key-1234567890123456".getBytes());
    }
}
