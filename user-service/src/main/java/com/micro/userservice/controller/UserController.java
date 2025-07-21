package com.micro.userservice.controller;

import com.micro.userservice.dto.AuthUserDTO;
import com.micro.userservice.dto.UserDTO;
import com.micro.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Separate DTO with password input for creation
    public static class UserCreateRequest {
        public String email;
        public String password;
        public String name;
        public java.util.List<String> roles;
    }

    //add  @Valid here to validate email and Password TODO
    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateRequest request) {
        UserDTO dto = new UserDTO();
        dto.setEmail(request.email);
        dto.setName(request.name);
        dto.setRoles(request.roles);
        userService.createUser(dto, request.password);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.getSafeUserDTOByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Internal endpoint for auth-service to fetch hashed password and roles
    @GetMapping("/internal/email/{email}")
    public ResponseEntity<AuthUserDTO> getAuthUserByEmail(@PathVariable String email) {
        return userService.getAuthUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}