package com.example.appholaagri.model.ChangePassModel;

public class ChangePassRequest {
    private String confirmPassword;
    private String oldPassword;
    private String password;

    // Constructor
    public ChangePassRequest() {

    }
    public ChangePassRequest(String confirmPassword, String oldPassword, String password) {
        this.confirmPassword = confirmPassword;
        this.oldPassword = oldPassword;
        this.password = password;
    }

    // Getters and Setters
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
