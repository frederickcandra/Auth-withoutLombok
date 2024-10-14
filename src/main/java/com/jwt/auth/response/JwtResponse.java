package com.jwt.auth.response;

import com.jwt.auth.model.Role;

import java.util.Objects;

public class JwtResponse {

    private String token;
    private String refreshToken;
    private String message;
    private Role role;

    // Constructor dengan parameter
    public JwtResponse(String token, String refreshToken, String message, Role role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.message = message;
        this.role = role;
    }

    // Default constructor
    public JwtResponse() {
    }

    // Getter dan setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}