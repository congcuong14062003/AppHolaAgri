package com.example.appholaagri.model.CheckPhoneModel;

public class CheckPhoneRequest {
    private String deviceId;
    private String userName;

    // Constructor
    public CheckPhoneRequest(String deviceId, String userName) {
        this.deviceId = deviceId;
        this.userName = userName;
    }

    // Getters and Setters
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
