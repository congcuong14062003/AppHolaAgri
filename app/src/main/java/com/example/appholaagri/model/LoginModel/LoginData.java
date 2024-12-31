package com.example.appholaagri.model.LoginModel;

public class LoginData {
    private String username;
    private boolean isFirstLogin;
    private int failLoginCount;
    private String token;
    private String urlRole; // Có thể null

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public int getFailLoginCount() {
        return failLoginCount;
    }

    public void setFailLoginCount(int failLoginCount) {
        this.failLoginCount = failLoginCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrlRole() {
        return urlRole;
    }

    public void setUrlRole(String urlRole) {
        this.urlRole = urlRole;
    }
}

