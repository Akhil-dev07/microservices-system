package com.micro.authservice.controller;

import com.micro.authservice.dto.AuthUserDTO;
import com.micro.authservice.dto.LoginRequest;
import com.micro.authservice.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    private final RestTemplate restTemplate = new RestTemplate();
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Call user-service to get user by email

           //String url = "http://localhost:8080/api/users/internal/email/" + request.getEmail();
/*
        restTemplate added as configuration class with @LoadBalanced to make localhost:8080-> user-service
*/
        String url = "http://user-service/api/users/internal/email/" + request.getEmail();

        ResponseEntity<AuthUserDTO> response = restTemplate.getForEntity(url, AuthUserDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        AuthUserDTO user = response.getBody();

        // Compare raw password with stored (hashed) password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate JWT
        String token = jwtService.generateToken(user.getEmail(), user.getRoles());

        return ResponseEntity.ok(token);
    }
}
