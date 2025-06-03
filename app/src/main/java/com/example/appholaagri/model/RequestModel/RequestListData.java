package com.example.appholaagri.model.RequestModel;

import java.util.List;

public class RequestListData {

    private List<RequestData> data;
    private int numOfRecords;
    private int totalRecord;

    public static class RequestData {
        private int requestId;
        private String requestName;
        private String requestTime;
        private String createdDate;
        private Status status;
        private int groupRequestType;
        private String groupRequestCode;
        private int isUrgent;
        private Employee employee;
        private int statusIdx;

        // Getters and Setters
        public int getRequestId() {
            return requestId;
        }

        public void setRequestId(int requestId) {
            this.requestId = requestId;
        }

        public String getRequestName() {
            return requestName;
        }

        public void setRequestName(String requestName) {
            this.requestName = requestName;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public int getGroupRequestType() {
            return groupRequestType;
        }

        public void setGroupRequestType(int groupRequestType) {
            this.groupRequestType = groupRequestType;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }

        public String getGroupRequestCode() {
            return groupRequestCode;
        }

        public void setGroupRequestCode(String groupRequestCode) {
            this.groupRequestCode = groupRequestCode;
        }

        public int getIsUrgent() {
            return isUrgent;
        }

        public void setIsUrgent(int isUrgent) {
            this.isUrgent = isUrgent;
        }

        public int getStatusIdx() {
            return statusIdx;
        }

        public void setStatusIdx(int statusIdx) {
            this.statusIdx = statusIdx;
        }
    }

    public static class Status {
        private int id;
        private String name;
        private String color;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static class Employee {
        private int employeeId;
        private String employeeName;
        private String employeeCode;
        private String employeeAvatar;

        // Getters and Setters
        public int getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeCode() {
            return employeeCode;
        }

        public void setEmployeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
        }

        public String getEmployeeAvatar() {
            return employeeAvatar;
        }

        public void setEmployeeAvatar(String employeeAvatar) {
            this.employeeAvatar = employeeAvatar;
        }
    }

    // Getters and Setters for RequestListModel
    public List<RequestData> getData() {
        return data;
    }

    public void setData(List<RequestData> data) {
        this.data = data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
}

