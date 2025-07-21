package com.micro.gateway_service.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey secretKey; // ✅ Inject the shared secret key used for validating JWTs

    public JwtAuthFilter(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // ✅ Get Authorization header from the request
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // ✅ If header is missing or doesn't start with "Bearer ", reject the request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        // ✅ Extract the token (without "Bearer ")
        String token = authHeader.substring(7);

        try {
            // ✅ Parse and validate the JWT token using the secret key
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // ✅ If token is valid, continue the request processing
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // ✅ If token is invalid or expired, return 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // ✅ Skip this filter for /api/auth/** endpoints (usually login/register)
        String path = request.getRequestURI();
        return path.startsWith("/api/auth");
    }
}