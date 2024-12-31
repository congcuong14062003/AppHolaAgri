package com.example.appholaagri.model.LoginModel;

public class LoginResponse {
    private int status;
    private int code;
    private String message;
    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Inner class for Data
    public static class Data {
        private String username;
        private boolean isFirstLogin;
        private int failLoginCount;
        private String token;

        // Getters and Setters for Data
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
    }
}
