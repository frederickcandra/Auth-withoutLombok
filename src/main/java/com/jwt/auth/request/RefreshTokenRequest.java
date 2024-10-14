package com.jwt.auth.request;

public class RefreshTokenRequest {
    private String username;
    private String refreshToken;

    public RefreshTokenRequest(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
