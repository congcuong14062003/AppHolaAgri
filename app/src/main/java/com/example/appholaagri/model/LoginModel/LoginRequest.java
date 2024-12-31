package com.example.appholaagri.model;

public class LoginRequest {
    private String deviceId;
    private int isMobile;
    private String password;
    private int rememberMe;
    private String requestId;
    private int serialVersionUID;
    private String userName;

    // Constructor
    public LoginRequest(String deviceId, int isMobile, String password, int rememberMe, String requestId, int serialVersionUID, String userName) {
        this.deviceId = deviceId;
        this.isMobile = isMobile;
        this.password = password;
        this.rememberMe = rememberMe;
        this.requestId = requestId;
        this.serialVersionUID = serialVersionUID;
        this.userName = userName;
    }

    // Getter and Setter for deviceId
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    // Getter and Setter for isMobile
    public int getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(int isMobile) {
        this.isMobile = isMobile;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for rememberMe
    public int getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(int rememberMe) {
        this.rememberMe = rememberMe;
    }

    // Getter and Setter for requestId
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    // Getter and Setter for serialVersionUID
    public int getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(int serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    // Getter and Setter for userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
