package com.example.appholaagri.model.ForgotPasswordModel;

public class ForgotPasswordRequest {

    private String deviceId;

    private int isMobile;

    private String password;

    private int rememberMe;

    private String requestId;

    private int serialVersionUID;

    private String userName;

    public ForgotPasswordRequest(String deviceId, int isMobile, String password, int rememberMe, String requestId, int serialVersionUID, String userName) {
        this.deviceId = deviceId;
        this.isMobile = isMobile;
        this.password = password;
        this.rememberMe = rememberMe;
        this.requestId = requestId;
        this.serialVersionUID = serialVersionUID;
        this.userName = userName;
    }

    // Getters and Setters

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(int isMobile) {
        this.isMobile = isMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(int rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(int serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

