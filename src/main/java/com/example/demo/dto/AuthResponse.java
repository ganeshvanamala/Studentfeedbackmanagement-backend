package com.example.demo.dto;

import com.example.demo.model.User;

public class AuthResponse {
    private User user;
    private String token;
    private Long expiresAt;

    public AuthResponse() {
    }

    public AuthResponse(User user, String token, Long expiresAt) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
