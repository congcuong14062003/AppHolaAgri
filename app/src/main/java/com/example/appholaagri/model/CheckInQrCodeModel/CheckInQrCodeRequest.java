package com.example.appholaagri.model.CheckInQrCodeModel;

public class CheckInQrCodeRequest {

    private String deviceId;
    private int isConfirmed;
    private String qrContent;
    private int shiftType;
    private int workShiftId;

    // Constructor
    public CheckInQrCodeRequest(String deviceId, int isConfirmed, String qrContent, int shiftType, int workShiftId) {
        this.deviceId = deviceId;
        this.isConfirmed = isConfirmed;
        this.qrContent = qrContent;
        this.shiftType = shiftType;
        this.workShiftId = workShiftId;
    }

    // Getter and Setter methods
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }

    public int getShiftType() {
        return shiftType;
    }

    public void setShiftType(int shiftType) {
        this.shiftType = shiftType;
    }

    public int getWorkShiftId() {
        return workShiftId;
    }

    public void setWorkShiftId(int workShiftId) {
        this.workShiftId = workShiftId;
    }
}
