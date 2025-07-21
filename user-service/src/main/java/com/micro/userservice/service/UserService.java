package com.micro.userservice.service;

import com.micro.userservice.dto.AuthUserDTO;
import com.micro.userservice.dto.UserDTO;
import com.micro.userservice.model.User;
import com.micro.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // BCryptPasswordEncoder to encrypt the password before storing
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(UserDTO userDTO, String rawPassword) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        // Encrypt the raw password before saving
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setName(userDTO.getName());
        user.setRoles(userDTO.getRoles());
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Convert entity to safe DTO (no password)
    public Optional<UserDTO> getSafeUserDTOByEmail(String email) {
        return getUserByEmail(email).map(user -> {
            UserDTO dto = new UserDTO();
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setRoles(user.getRoles());
            return dto;
        });
    }

    // Convert entity to internal auth DTO (includes password)
    public Optional<AuthUserDTO> getAuthUserByEmail(String email) {
        return getUserByEmail(email).map(user -> {
            AuthUserDTO dto = new AuthUserDTO();
            dto.setEmail(user.getEmail());
            dto.setPassword(user.getPassword());
            dto.setRoles(user.getRoles());
            return dto;
        });
    }
}