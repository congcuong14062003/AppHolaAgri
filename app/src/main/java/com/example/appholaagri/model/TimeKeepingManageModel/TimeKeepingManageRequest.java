package com.example.appholaagri.model.TimeKeepingManageModel;

import java.util.List;

public class TimeKeepingManageRequest {
    private int clientType;
    private List<Integer> departmentId;
    private String endDate;
    private String keySearch;
    private List<Integer> managerId;
    private List<Integer> staffId;
    private String startDate;
    private List<Integer> status;
    private List<Integer> teamId;

    // Constructor
    public TimeKeepingManageRequest(int clientType, List<Integer> departmentId, String endDate, String keySearch,
                        List<Integer> managerId, List<Integer> staffId, String startDate,
                        List<Integer> status, List<Integer> teamId) {
        this.clientType = clientType;
        this.departmentId = departmentId;
        this.endDate = endDate;
        this.keySearch = keySearch;
        this.managerId = managerId;
        this.staffId = staffId;
        this.startDate = startDate;
        this.status = status;
        this.teamId = teamId;
    }

    // Getters and Setters
    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public List<Integer> getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(List<Integer> departmentId) {
        this.departmentId = departmentId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public List<Integer> getManagerId() {
        return managerId;
    }

    public void setManagerId(List<Integer> managerId) {
        this.managerId = managerId;
    }

    public List<Integer> getStaffId() {
        return staffId;
    }

    public void setStaffId(List<Integer> staffId) {
        this.staffId = staffId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public List<Integer> getTeamId() {
        return teamId;
    }

    public void setTeamId(List<Integer> teamId) {
        this.teamId = teamId;
    }
}
