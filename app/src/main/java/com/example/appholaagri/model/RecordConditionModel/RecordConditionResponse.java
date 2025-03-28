package com.example.appholaagri.model.RecordConditionModel;

import java.util.List;

public class RecordConditionResponse {

    private List<RecordData> data;
    private int numOfRecords;
    private int totalRecord;

    // Getters and Setters

    public List<RecordData> getData() {
        return data;
    }

    public void setData(List<RecordData> data) {
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

    // RecordData chứa thông tin chi tiết từng bản ghi
    public static class RecordData {
        private int id;
        private Employee employee;
        private String atDate;
        private Team team;
        private CropVarieties cropVarieties;
        private CultivationArea cultivationArea;
        private Plantation plantation;
        private String problem;
        private String development;
        private int status;
        private String statusName;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }

        public String getAtDate() {
            return atDate;
        }

        public void setAtDate(String atDate) {
            this.atDate = atDate;
        }

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }

        public CropVarieties getCropVarieties() {
            return cropVarieties;
        }

        public void setCropVarieties(CropVarieties cropVarieties) {
            this.cropVarieties = cropVarieties;
        }

        public CultivationArea getCultivationArea() {
            return cultivationArea;
        }

        public void setCultivationArea(CultivationArea cultivationArea) {
            this.cultivationArea = cultivationArea;
        }

        public Plantation getPlantation() {
            return plantation;
        }

        public void setPlantation(Plantation plantation) {
            this.plantation = plantation;
        }

        public String getProblem() {
            return problem;
        }

        public void setProblem(String problem) {
            this.problem = problem;
        }

        public String getDevelopment() {
            return development;
        }

        public void setDevelopment(String development) {
            this.development = development;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }
    }
    // Employee - Thông tin nhân viên
    public static class Employee {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;
        private String url;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    // Team - Thông tin tổ nhóm
    public static class Team {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    // CropVarieties - Thông tin giống cây trồng
    public static class CropVarieties {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    // CultivationArea - Thông tin khu canh tác
    public static class CultivationArea {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    // Plantation - Thông tin vườn trồng
    public static class Plantation {
        private int id;
        private String code;
        private String name;
        private int status;
        private String color;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}

