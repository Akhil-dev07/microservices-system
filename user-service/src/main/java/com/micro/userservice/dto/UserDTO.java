package com.micro.userservice.dto;

import java.util.List;

public class UserDTO {
    private String email;
    private String name;
    private List<String> roles;

    // No password field in this DTO to ensure safe response

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

