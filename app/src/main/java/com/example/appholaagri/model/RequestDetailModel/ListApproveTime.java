package com.example.appholaagri.model.RequestDetailModel;

import java.io.Serializable;

public class ListApproveTime implements Serializable {
    private String time;
    private String approverName;
    private String status;

    // Getters and Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
