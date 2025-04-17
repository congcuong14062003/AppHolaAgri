package com.example.appholaagri.model.RecordConditionModel;

import android.net.Uri;

import java.util.List;

public class InforRecordConditionByQrCode {
    private Integer plantExaminationId;
    private Integer plantId;
    private String plantCode;
    private Employee employee;
    private Plantation plantation;
    private CultivationArea cultivationArea;
    private CropVarieties cropVarieties;
    private Team team;
    private String problem;
    private String development;
    private List<String> fileAttachment;  // Chuyển List<Object> thành List<Uri> // Assuming fileAttachment is a list of objects (adjust type if needed)
    private Status status;
    private List<ListStatus> listStatus;
    private Approve approve;
    private String reason;
    private Integer employeeId;

    // Getters and Setters

    public Integer getPlantExaminationId() {
        return plantExaminationId;
    }

    public void setPlantExaminationId(Integer plantExaminationId) {
        this.plantExaminationId = plantExaminationId;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Plantation getPlantation() {
        return plantation;
    }

    public void setPlantation(Plantation plantation) {
        this.plantation = plantation;
    }

    public CultivationArea getCultivationArea() {
        return cultivationArea;
    }

    public void setCultivationArea(CultivationArea cultivationArea) {
        this.cultivationArea = cultivationArea;
    }

    public CropVarieties getCropVarieties() {
        return cropVarieties;
    }

    public void setCropVarieties(CropVarieties cropVarieties) {
        this.cropVarieties = cropVarieties;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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


    public List<String> getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(List<String> fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<ListStatus> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<ListStatus> listStatus) {
        this.listStatus = listStatus;
    }

    public Approve getApprove() {
        return approve;
    }

    public void setApprove(Approve approve) {
        this.approve = approve;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    public static  class Status {
        private Integer id;
        private String code;
        private String name;
        private Integer status;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static class Employee {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;
        private String atDate;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getAtDate() {
            return atDate;
        }

        public void setAtDate(String atDate) {
            this.atDate = atDate;
        }
    }
    public static class Plantation {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
    public static class CultivationArea {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
    public static class CropVarieties {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
    public static class Team {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
    public static class ListStatus {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
    public static class Approve {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String color;
        private String atDate;

        // Getters and Setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getAtDate() {
            return atDate;
        }

        public void setAtDate(String atDate) {
            this.atDate = atDate;
        }
    }

}
