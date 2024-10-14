package com.jwt.auth.model;

import java.util.Objects;

public class UserRedis {

    private String username;
    private String password;
    private String token;
    private String refreshToken;
    private Role role;

    // Constructor dengan parameter
    public UserRedis(String username, String password, String token, String refreshToken, Role role) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    // Default constructor
    public UserRedis() {}

    // Getter dan setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // equals dan hashCode methods untuk perbandingan
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRedis userRedis = (UserRedis) o;
        return Objects.equals(username, userRedis.username) &&
                Objects.equals(password, userRedis.password) &&
                Objects.equals(token, userRedis.token) &&
                Objects.equals(refreshToken, userRedis.refreshToken) &&
                role == userRedis.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, token, refreshToken, role);
    }

    // toString method untuk tujuan debugging
    @Override
    public String toString() {
        return "UserRedis{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", role=" + role +
                '}';
    }
}
