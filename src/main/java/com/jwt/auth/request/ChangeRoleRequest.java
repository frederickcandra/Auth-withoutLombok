package com.jwt.auth.request;

import com.jwt.auth.model.Role;

public class ChangeRoleRequest {
    private String username;
    private Role role;

    public ChangeRoleRequest(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public ChangeRoleRequest(){

    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
