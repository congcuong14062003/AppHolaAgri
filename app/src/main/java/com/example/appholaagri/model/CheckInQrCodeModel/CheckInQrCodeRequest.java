package com.example.appholaagri.model.CheckInQrCodeModel;

public class CheckInQrCodeRequest {

    private String deviceId;
    private int isConfirmed;
    private String qrContent;
    private String reason;
    private int reasonType;
    private int shiftType;
    private int workShiftId;


    // Constructor
    public CheckInQrCodeRequest(String deviceId, int isConfirmed, String qrContent, String reason, int reasonType, int shiftType, int workShiftId) {
        this.deviceId = deviceId;
        this.isConfirmed = isConfirmed;
        this.qrContent = qrContent;
        this.reason = reason;
        this.reasonType = reasonType;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getReasonType() {
        return reasonType;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
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
